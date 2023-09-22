package com.tokopedia.scp_rewards_common.utils

import android.media.MediaPlayer
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioFactory private constructor(){
    private var mMediaPlayer:MediaPlayer? = null
    private var current = 0L
    private var isAudioLoading = false
    private var errorListener:(() -> Unit)? = null
    companion object{
        private const val BASE_TIMEOUT = 1500L
        fun createAudioFromUrl(url:String,loop:Boolean = false,onPrepare:(() -> Unit)?=null,onError:(() -> Unit)?=null) : AudioFactory {
            return AudioFactory().apply {
                errorListener = onError
                mMediaPlayer = MediaPlayer().apply {
                    isLooping = loop
                    isAudioLoading = true
                    try {
                        setDataSource(url)
                        setOnPreparedListener {
                            if (isAudioLoading) {
                                isAudioLoading = false
                                onPrepare?.invoke()
                            }
                        }
                        setOnErrorListener { _, _, _ ->
                            isAudioLoading = false
                            onError?.invoke()
                            false
                        }
                        current = System.currentTimeMillis()
                        prepareAsync()
                    } catch (err: Throwable) {
                        onError?.invoke()
                        FirebaseCrashlytics.getInstance().recordException(err)
                    }
                }
            }
        }
    }

    fun withTimeout(timeout:Long = BASE_TIMEOUT) : AudioFactory {
        CoroutineScope(Dispatchers.Main).launch {
            delay(timeout)
            if (isAudioLoading) {
                isAudioLoading = false
                errorListener?.invoke()
                errorListener = null
                releaseMediaPlayer()
            }
        }
        return this
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

