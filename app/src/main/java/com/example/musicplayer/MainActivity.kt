package com.example.musicplayer

import android.content.Context
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import java.util.ArrayList

class custom : ArrayAdapter<String>
{
    constructor(c:Context,title:ArrayList<String>) : super(c,android.R.layout.simple_list_item_1,title) {

    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var arrayAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, listSongTitle)
        list_song.setAdapter(arrayAdapter)
        list_song.setOnItemClickListener { adapterView, view, index, l ->
            view.setBackgroundColor(Color.rgb(255,0,0))
            this.index = index
            changeSong(index)
        }
        mediaPlayer = MediaPlayer.create(applicationContext, listSong[0])

        class ListenSeekBar : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                when(p0?.id)
                {
                    R.id.seek_bar_position ->
                        if (p2)
                            mediaPlayer.seekTo(p1 * 1000)
                    R.id.seek_bar_volume ->
                        mediaPlayer.setVolume(p1/100.0f,p1/100.0f)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        }

        var listener = ListenSeekBar()
        seek_bar_volume.setOnSeekBarChangeListener(listener)
        seek_bar_position.setOnSeekBarChangeListener(listener)
    }

    private lateinit var mediaPlayer: MediaPlayer
    private var listSong = listOf(R.raw.snake_the_world, R.raw.my_life)
    private var listSongTitle = listOf("Shake the world","My Life")
    private var index = 0
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()

    fun click(view:View)
    {
        when(view.id)
        {
            R.id.btnPre -> {
                index ++
                if(index == listSong.size)
                    index = 0
                changeSong(index)
            }
            R.id.btnplay ->
                if(mediaPlayer.isPlaying) pauseSong() else playSong()
            R.id.btnNext -> {
                index --
                if(index < 0)
                    index = listSong.size -1
                changeSong(index)
            }
        }
    }

    private fun playSong()
    {
        setPositionSeekBar()
        mediaPlayer.start()
        btnplay.text = "Pause"
    }

    private fun pauseSong()
    {
        mediaPlayer.pause()
        btnplay.text = "Play"
    }

    private fun changeSong(index:Int)
    {
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(applicationContext,listSong[index])
        playSong()
    }

    private fun setPositionSeekBar() {
        seek_bar_position.max = mediaPlayer.duration/1000
        runnable = Runnable {
            seek_bar_position.progress = mediaPlayer.currentPosition/1000
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }
}
