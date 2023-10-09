package com.tokopedia.stories.view.utils

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.Toaster
import java.lang.Math.toDegrees
import kotlin.math.atan2

internal fun View.onTouchEventStories(
    eventAction: (event: TouchEventStories) -> Unit,
) {
    var longPressState = false

    val gestureDetector by lazyThreadSafetyNone {
        GestureDetectorCompat(this.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                val angle = toDegrees(atan2(e1.x - e2.y, e2.x - e1.x).toDouble()).toFloat()
                if (angle > 45 && angle <= 135) eventAction.invoke(TouchEventStories.SWIPE_UP)
                return false
            }
        })
    }

    setOnLongClickListener {
        longPressState = true
        eventAction.invoke(TouchEventStories.PAUSE)
        true
    }
    setOnTouchListener { _, p1 ->
        performClick()
        when (p1?.action) {
            MotionEvent.ACTION_UP -> {
                if (longPressState) {
                    longPressState = false
                    eventAction.invoke(TouchEventStories.RESUME)
                } else eventAction.invoke(TouchEventStories.NEXT_PREV)
            }
            MotionEvent.ACTION_CANCEL -> eventAction.invoke(TouchEventStories.RESUME)
            else -> {}
        }
        gestureDetector.onTouchEvent(p1)
        false
    }
}

internal enum class TouchEventStories {
    PAUSE, RESUME, NEXT_PREV, SWIPE_UP
}

internal fun View.showToaster(
    message: String,
    type: Int = Toaster.TYPE_NORMAL,
    actionText: String = "",
    bottomHeight: Int = 0,
    clickListener: View.OnClickListener = View.OnClickListener {}
) {
    Toaster.toasterCustomBottomHeight = bottomHeight
    Toaster.build(
        this,
        message,
        type = type,
        actionText = actionText,
        clickListener = clickListener
    ).show()
}

internal fun Int.getRandomNumber(): Int {
    val oldValue = this
    val newValue = (1 until 100).random()
    return if (oldValue == newValue) newValue.plus(1) else newValue
}

internal const val KEY_CONFIG_ENABLE_STORIES_ROOM = "android_enable_content_stories_room"
internal const val KEY_ARGS = "shop_id"
internal const val ARGS_SOURCE = "source"
internal const val ARGS_SOURCE_ID = "source_id"
internal const val ARGS_ENTRY_POINT = "entrypoint"
internal const val TAG_FRAGMENT_STORIES_GROUP = "fragment_stories_group"
internal const val TAG_FRAGMENT_STORIES_DETAIL = "fragment_stories_detail"
