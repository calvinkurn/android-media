package com.tokopedia.troubleshooter.notification.data.service.ringtone

import android.content.Context
import android.media.AudioManager

class RingtoneModeServiceImpl(
        private val context: Context
): RingtoneModeService {

    override fun isRing(): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> true
            AudioManager.RINGER_MODE_SILENT -> false
            AudioManager.RINGER_MODE_VIBRATE -> false
            else -> false
        }
    }

}