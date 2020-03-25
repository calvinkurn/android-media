package com.tokopedia.gamification.audio

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.util.Log


class AudioManager(var mSource: Int = -1, var mContext: Context, var isLoop: Boolean = false) {
    var mPlayer: MediaPlayer? = null

    init {
        mPlayer = MediaPlayer()
    }

    public fun playAudio() {
        mPlayer?.apply {
            setOnPreparedListener { mp ->
                mp.start()
                Log.e("DailyGiftBox", "Started");
            }

            setOnCompletionListener { mp ->
                if (!isLooping) {
                    mp.release()
                    mPlayer = null;
                }

                Log.e("DailyGiftBox", "Completed");
            }

            isLooping = isLoop

            if (isPlaying) {
                stop()
            }

            reset()

            val afd: AssetFileDescriptor = mContext.getResources().openRawResourceFd(mSource)
                    ?: return

            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()

            prepareAsync()
        }
    }

    public fun destroy() {
        mPlayer?.release()
        mPlayer = null
    }
}