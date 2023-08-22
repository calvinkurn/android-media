package com.tokopedia.stories.view.utils

import android.view.MotionEvent
import android.view.View

internal fun View.onTouchEventStory(
    eventAction: (event: TouchEventStory) -> Unit,
) {
    var longPressState = false

    setOnLongClickListener {
        longPressState = true
        eventAction.invoke(TouchEventStory.PAUSE)
        true
    }
    setOnTouchListener { _, p1 ->
        performClick()
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (longPressState) {
                longPressState = false
                eventAction.invoke(TouchEventStory.RESUME)
            } else eventAction.invoke(TouchEventStory.NEXT_PREV)
        }
        false
    }
}

enum class TouchEventStory {
    PAUSE, RESUME, NEXT_PREV
}
