package com.tokopedia.stories.view.utils

import android.view.MotionEvent
import android.view.View

internal fun View.onPauseEventStories(
    isPaused: (Boolean) -> Unit,
) {
    var longPressState = false

    setOnLongClickListener {
        longPressState = true
        isPaused.invoke(true)
        true
    }
    setOnTouchListener { _, p1 ->
        performClick()
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (longPressState) {
                longPressState = false
                isPaused.invoke(false)
            }
        }
        false
    }
}
