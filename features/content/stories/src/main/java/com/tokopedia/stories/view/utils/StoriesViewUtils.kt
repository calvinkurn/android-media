package com.tokopedia.stories.view.utils

import android.view.MotionEvent
import android.view.View

internal fun View.onViewEventStories(
    eventAction: (event: StoriesViewEvent) -> Unit,
) {
    var longPressState = false

    setOnLongClickListener {
        longPressState = true
        eventAction.invoke(StoriesViewEvent.PAUSE)
        true
    }
    setOnTouchListener { _, p1 ->
        performClick()
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (longPressState) {
                longPressState = false
                eventAction.invoke(StoriesViewEvent.RESUME)
            } else eventAction.invoke(StoriesViewEvent.NEXT_PREV)
        }
        false
    }
}

enum class StoriesViewEvent {
    PAUSE, RESUME, NEXT_PREV
}
