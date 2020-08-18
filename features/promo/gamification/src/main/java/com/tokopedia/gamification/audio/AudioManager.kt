package com.tokopedia.gamification.audio

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import timber.log.Timber


class AudioManager(var mContext: Context) {
    var mPlayer: MediaPlayer? = null

    init {
        mPlayer = MediaPlayer()
    }

    fun playAudio(filePath: String, isLoop: Boolean = false) {
        try {
            mPlayer?.apply {
                setOnPreparedListener { mp ->
                    mp.start()
                    isLooping = isLoop
                }
                if (isPlaying) {
                    stop()
                }
                reset()
                setDataSource(filePath)
                prepareAsync()
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun playAudio(resId: Int, isLoop: Boolean = false) {
        if (resId <= 0) {
            throw IllegalArgumentException("Please provide correct audio file")
        }

        mPlayer?.apply {
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
        }
    }

    fun destroy() {
        mPlayer?.release()
        mPlayer = null
    }
}