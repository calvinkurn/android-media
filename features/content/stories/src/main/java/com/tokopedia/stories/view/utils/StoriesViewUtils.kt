package com.tokopedia.stories.view.utils

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.Toaster
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
                val angle: Float = Math.toDegrees(atan2(e1.x - e2.y, e2.x - e1.x).toDouble()).toFloat()
                if (angle > 45 && angle <= 135) {
                    eventAction.invoke(TouchEventStories.SWIPE_UP)
                }
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
        if (p1?.action == MotionEvent.ACTION_UP) {
            if (longPressState) {
                longPressState = false
                eventAction.invoke(TouchEventStories.RESUME)
            } else eventAction.invoke(TouchEventStories.NEXT_PREV)
        }
        gestureDetector.onTouchEvent(p1)
        false
    }
}

enum class TouchEventStories {
    PAUSE, RESUME, NEXT_PREV, SWIPE_UP
}

internal fun View.showToaster(
    message: String,
    type: Int = Toaster.TYPE_NORMAL,
    actionText: String = "",
    clickListener: View.OnClickListener = View.OnClickListener {}
) {
    Toaster.build(
        this,
        message,
        type = type,
        actionText = actionText,
        clickListener = clickListener
    ).show()
}
