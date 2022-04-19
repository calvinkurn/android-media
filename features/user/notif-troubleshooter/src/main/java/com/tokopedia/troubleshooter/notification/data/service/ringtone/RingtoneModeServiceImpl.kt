package com.tokopedia.troubleshooter.notification.data.service.ringtone

import android.content.Context
import android.media.AudioManager
import com.tokopedia.troubleshooter.notification.ui.state.RingtoneState

class RingtoneModeServiceImpl(
        private val context: Context
): RingtoneModeService {

    override fun isRing(): RingtoneState {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> RingtoneState.Normal
            AudioManager.RINGER_MODE_SILENT -> RingtoneState.Silent
            AudioManager.RINGER_MODE_VIBRATE -> RingtoneState.Vibrate
            else -> RingtoneState.Silent
        }
    }

}