package com.tokopedia.gamification.audio

import android.content.Context
import java.lang.IllegalArgumentException

class AudioFactory {
    companion object {
        fun createAudio(context: Context): AudioManager {

            return AudioManager(context)
        }
    }
}