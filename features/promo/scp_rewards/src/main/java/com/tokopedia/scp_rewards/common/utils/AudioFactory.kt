package com.tokopedia.scp_rewards.common.utils

import android.content.Context
import android.media.MediaPlayer
import java.io.FileNotFoundException
import java.io.IOException

class AudioFactory{
    private var mMediaPlayer:MediaPlayer? = null
    fun createAudioFromUrl(url:String,loop:Boolean = false,onPrepare:(() -> Unit)?=null,onError:(() -> Unit)?=null) {

        mMediaPlayer = MediaPlayer().apply {
            isLooping = loop
            try{
                setDataSource(url)
            }
            catch (err:Throwable){
                onError?.invoke()
            }

          setOnPreparedListener {
             onPrepare?.invoke()
          }
            setOnErrorListener { _,_,_ ->
                onError?.invoke()
                false
            }
            prepareAsync()
        }
    }

    fun playAudio(){
        if(mMediaPlayer==null){
            throw IllegalStateException("Initialize media player first before playing audio")
        }
        mMediaPlayer?.start()
    }

    fun releaseMediaPlayer(){
        mMediaPlayer?.release()
        mMediaPlayer = null
    }
}

