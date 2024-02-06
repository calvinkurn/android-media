package com.tokopedia.notifications.inApp.ketupat

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log


class AudioManager(var mContext: Context) {
    var mPlayer: MediaPlayer? = null

    init {
        mPlayer = MediaPlayer()
    }

    fun playAudio(filePath: String, isLoop: Boolean = false) {

        mPlayer?.apply {
            try {
                setOnPreparedListener { mp ->
                    mp.start()
                    isLooping = isLoop
                }
                if (isPlaying) {
                    stop()
                }
                reset()
                setDataSource(filePath)
                prepare()

            } catch (ex: Exception) {
                Log.e("aa",ex.toString())
            }
        }
    }
    fun playAudio(resId: Int, isLoop: Boolean = false) {
        if (resId <= 0) {
            throw IllegalArgumentException("Please provide correct audio file")
        }

        mPlayer?.apply {
            try {
                setOnPreparedListener { mp ->
                    mp.start()
                    isLooping = isLoop
                }

                if (isPlaying) {
                    stop()
                }
                reset()
                val afd: AssetFileDescriptor = mContext.resources.openRawResourceFd(resId)
                        ?: return
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                afd.close()
                prepareAsync()
            } catch (ex: Exception) {
                Log.e("aa",ex.toString())
            }
        }
    }

    fun destroy() {
        mPlayer?.release()
        mPlayer = null
    }
}
