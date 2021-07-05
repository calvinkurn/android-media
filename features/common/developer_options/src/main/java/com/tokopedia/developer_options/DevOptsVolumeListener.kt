package com.tokopedia.developer_options

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_VOLUME_DOWN
import android.view.KeyEvent.KEYCODE_VOLUME_UP
import com.tokopedia.abstraction.base.view.listener.DebugVolumeListener
import com.tokopedia.developer_options.presentation.activity.DeveloperOptionActivity
import java.lang.ref.WeakReference

class DevOptsVolumeListener(activity: Activity) : DebugVolumeListener {
    var activityRef: WeakReference<Activity>? = null
    init {
        activityRef = WeakReference(activity)
    }

    val VOLUME_UP = true
    val VOLUME_DOWN = false

    val TRIGGER_SEQUENCE = listOf(VOLUME_UP, VOLUME_UP, VOLUME_DOWN, VOLUME_DOWN)

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
                    goToDeveloperOptions()
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

    private fun goToDeveloperOptions(){
        val activity = activityRef?.get() ?: return
        if (!activity.isFinishing && !activity.isDestroyed) {
            activity.startActivity(Intent(activity, DeveloperOptionActivity::class.java))
        }
    }
}