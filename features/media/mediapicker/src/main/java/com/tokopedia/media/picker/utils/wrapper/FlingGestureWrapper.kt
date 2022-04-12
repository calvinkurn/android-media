package com.tokopedia.media.picker.utils.wrapper

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class FlingGestureWrapper(
    private val swipeLeftToRight: () -> Unit,
    private val swipeRightToLeft: () -> Unit,
) : GestureDetector.OnGestureListener {
    override fun onDown(e: MotionEvent?) = true

    override fun onSingleTapUp(e: MotionEvent?) = true

    override fun onShowPress(e: MotionEvent?) {}

    override fun onLongPress(e: MotionEvent?) {}

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, dX: Float, dY: Float) = false

    override fun onFling(
        start: MotionEvent,
        finish: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (abs(start.y - finish.y) > swipeMaxOff) return false

        if ((start.x - finish.x) > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
            swipeLeftToRight()
        } else if ((finish.x - start.x) > swipeMinDistance && abs(velocityX) > swipeThresholdVelocity) {
            swipeRightToLeft()
        }

        return true
    }

    companion object {
        private const val swipeMaxOff = 250
        private const val swipeMinDistance = 120
        private const val swipeThresholdVelocity = 200
    }
}