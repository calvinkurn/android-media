package com.tokopedia.stories.view.utils

import android.view.MotionEvent
import android.view.View

internal fun View.onTouchEventStories(
    eventAction: (event: TouchEventStories) -> Unit,
) {
    var longPressState = false

    setOnLongClickListener {
        longPressState = true
        eventAction.invoke(TouchEventStories.PAUSE)
        true
    }
    setOnTouchListener { _, p1 ->
        performClick()
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (longPressState) {
                longPressState = false
                eventAction.invoke(TouchEventStories.RESUME)
            } else eventAction.invoke(TouchEventStories.NEXT_PREV)
        }
        false
    }
}

enum class TouchEventStories {
    PAUSE, RESUME, NEXT_PREV
}

const val SHOP_ID = "shop_id"
