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

    private val handler = Handler(Looper.getMainLooper())
    private var dismissRunnable: Runnable? = null

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

        dismissRunnable?.let { handler.removeCallbacks(it) } // cancel previous task
        val newRunnable = Runnable { icAudio.hide() } // create a new runnable task
        handler.postDelayed(newRunnable, 2_000) // Schedule the task

        // store a runnable reference
        dismissRunnable = newRunnable
    }
}
