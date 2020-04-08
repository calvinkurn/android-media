package com.tokopedia.gamification.audio

import android.content.Context

class AudioFactory {
    companion object {
        fun createAudio(context: Context): AudioManager {

            return AudioManager(context)
        }
    }
}