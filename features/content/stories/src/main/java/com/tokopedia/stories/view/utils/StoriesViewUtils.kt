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

internal enum class TouchEventStories {
    PAUSE, RESUME, NEXT_PREV
}

internal const val SHOP_ID = "shop_id"
internal const val SHOP_ID_INDEX_APP_LINK = 1
internal const val STORY_GROUP_ID = "stories_group_id"
internal const val TAG_FRAGMENT_STORIES_GROUP = "fragment_stories_group"
internal const val TAG_FRAGMENT_STORIES_DETAIL = "fragment_stories_detail"
