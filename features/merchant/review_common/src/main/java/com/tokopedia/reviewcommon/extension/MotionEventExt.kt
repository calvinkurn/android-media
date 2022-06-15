package com.tokopedia.reviewcommon.extension

import android.view.MotionEvent
import android.view.View
import com.tokopedia.kotlin.extensions.view.isVisible

fun MotionEvent.intersectWith(view: View): Boolean {
    return if (view.isVisible) {
        val reviewDetailCoordinate = IntArray(2).also {
            view.getLocationOnScreen(it)
        }
        val reviewDetailStartX = reviewDetailCoordinate.first()
        val reviewDetailEndX = reviewDetailCoordinate.first() + view.width
        val reviewDetailStartY = reviewDetailCoordinate.last()
        val reviewDetailEndY = reviewDetailCoordinate.last() + view.height
        x >= reviewDetailStartX && x <= reviewDetailEndX && y >= reviewDetailStartY && y <= reviewDetailEndY
    } else false
}