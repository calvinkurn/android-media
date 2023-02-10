package com.tokopedia.reviewcommon.extension

import android.view.MotionEvent
import android.view.View
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible

fun MotionEvent.intersectWith(view: View, extraSizePx: Long): Boolean {
    return if (view.isVisible && view.height.isMoreThanZero() && view.width.isMoreThanZero()) {
        val viewCoordinate = IntArray(2).also {
            view.getLocationOnScreen(it)
        }
        val viewStartX = (viewCoordinate.first() - extraSizePx).coerceAtLeast(0L)
        val viewEndX = viewCoordinate.first() + view.width + extraSizePx
        val viewStartY = (viewCoordinate.last() - extraSizePx).coerceAtLeast(0L)
        val viewEndY = viewCoordinate.last() + view.height + extraSizePx
        rawX >= viewStartX && rawX <= viewEndX && rawY >= viewStartY && rawY <= viewEndY
    } else false
}
