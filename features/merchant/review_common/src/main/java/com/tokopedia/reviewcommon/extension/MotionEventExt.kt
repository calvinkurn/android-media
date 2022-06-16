package com.tokopedia.reviewcommon.extension

import android.view.MotionEvent
import android.view.View
import com.tokopedia.kotlin.extensions.view.isVisible

fun MotionEvent.intersectWith(view: View, extraSizePx: Long = 50L): Boolean {
    return if (view.isVisible) {
        val viewCoordinate = IntArray(2).also {
            view.getLocationOnScreen(it)
        }
        val viewStartX = (viewCoordinate.first() - extraSizePx).coerceAtLeast(0L)
        val viewEndX = viewCoordinate.first() + view.width + extraSizePx
        val viewStartY = (viewCoordinate.last() - extraSizePx).coerceAtLeast(0L)
        val viewEndY = viewCoordinate.last() + view.height + extraSizePx
        x >= viewStartX && x <= viewEndX && y >= viewStartY && y <= viewEndY
    } else false
}