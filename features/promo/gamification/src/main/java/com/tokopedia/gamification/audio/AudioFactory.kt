package com.tokopedia.gamification.audio

import android.content.Context
import java.lang.IllegalArgumentException

class AudioFactory {
    companion object {
        fun createAudio(context: Context, res: Int, isLoop: Boolean = false): AudioManager {
            if (res <= 0) {
                throw IllegalArgumentException("Please provide correct audio file")
            }

            return AudioManager(res, context, isLoop)
        }
    }
}