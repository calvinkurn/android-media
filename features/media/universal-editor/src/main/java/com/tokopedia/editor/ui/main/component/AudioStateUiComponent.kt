package com.tokopedia.editor.ui.main.component

import android.os.Handler
import android.os.Looper
import android.view.HapticFeedbackConstants
import android.view.ViewGroup
import com.tokopedia.editor.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.common.basecomponent.UiComponent

class AudioStateUiComponent constructor(
    parent: ViewGroup
) : UiComponent(parent, R.id.uc_audio_removal_state) {

    private val icAudio: IconUnify = findViewById(R.id.ic_audio_state)


    fun onShowOrHideAudioState(isRemoved: Boolean) {
        if (isRemoved) muteState() else unMuteState()
    }

    private fun muteState() {
        icAudio.setImage(IconUnify.VOLUME_MUTE)
        enableHapticFeedback()
        waitToDismiss()
    }

    private fun unMuteState() {
        icAudio.setImage(IconUnify.VOLUME_UP)
        enableHapticFeedback()
        waitToDismiss()
    }

    private fun enableHapticFeedback() {
        container().performHapticFeedback(
            HapticFeedbackConstants.VIRTUAL_KEY,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    private fun waitToDismiss() {
        icAudio.show()

        Handler(Looper.getMainLooper()).postDelayed({
            icAudio.hide()
        }, 2_000)
    }
}
