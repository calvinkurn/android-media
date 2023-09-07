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

internal fun Int.getRandomNumber(): Int {
    val oldValue = this
    val newValue = (1 until 100).random()
    return if (oldValue == newValue) newValue.plus(1) else newValue
}

enum class TouchEventStories {
    PAUSE, RESUME, NEXT_PREV
}

const val SHOP_ID = "shop_id"
const val SHOP_ID_INDEX_APP_LINK = 1
const val STORY_GROUP_ID = "stories_group_id"
const val FRAGMENT_GROUP_TAG = "stories_group_fragment"
const val FRAGMENT_DETAIL_TAG = "stories_detail_fragment"
