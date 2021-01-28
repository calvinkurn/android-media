package com.tokopedia.prereleaseinspector

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.KeyEvent.KEYCODE_VOLUME_UP
import com.tokopedia.abstraction.base.view.listener.DebugVolumeListener
import java.lang.ref.WeakReference

class ViewInspectorVolumeListener(activity: Activity) : DebugVolumeListener {
    var activityRef: WeakReference<Activity>? = null
    init {
        activityRef = WeakReference(activity)
    }

    val VOLUME_UP = true
    val VOLUME_DOWN = false

    val TRIGGER_SEQUENCE = listOf(VOLUME_UP, VOLUME_DOWN, VOLUME_UP, VOLUME_DOWN)

    var triggerSequenceStep = 0
    var inputList = mutableListOf<Boolean>()

    override fun onKeyUp(keyCode: Int, event: KeyEvent?) {
        val input = when (keyCode) {
            KEYCODE_VOLUME_DOWN -> VOLUME_DOWN
            KEYCODE_VOLUME_UP -> VOLUME_UP
            else -> null
        }
        if (input != null) {
            triggerSequenceStep++
            inputList.add(input)
            if (TRIGGER_SEQUENCE[triggerSequenceStep - 1] == input) {
                if (triggerSequenceStep == TRIGGER_SEQUENCE.size) {
                    triggerSequenceStep = 0
                    inputList.clear()
                    showPopUp()
                }
            } else {
                while (triggerSequenceStep > 0) {
                    inputList.removeFirst()
                    triggerSequenceStep--
                    if (triggerSequenceStep > 0 && TRIGGER_SEQUENCE.subList(0, triggerSequenceStep) == inputList) {
                        break
                    }
                }
            }
        }
    }

    private fun showPopUp(){
        val activity = activityRef?.get() ?: return
        if (!activity.isFinishing && !activity.isDestroyed) {
            ViewInspectorManager.showPopupDialog(activity)
        }
    }
}