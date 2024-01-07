package com.tokopedia.notifications.inApp.ketupat

import android.content.Context

open class AudioFactory {
    companion object {
        fun createAudio(context: Context): AudioManager {

            return AudioManager(context)
        }
    }
}
