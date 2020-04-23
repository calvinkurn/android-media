package com.tokopedia.play.gesture

import android.view.MotionEvent
import android.view.View

/**
 * Created by jegul on 23/04/20
 */
class PlayClickTouchListener(private val tolerance: Int) : View.OnTouchListener {

    private var currentX = -1
    private var currentY = -1

    private val toleranceXRange: IntRange
        get() = currentX - tolerance .. currentX + tolerance

    private val toleranceYRange: IntRange
        get() = currentY - tolerance .. currentY + tolerance

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentX = event.x.toInt()
                currentY = event.y.toInt()
                false
            }
            MotionEvent.ACTION_UP -> {
                val upX = event.x.toInt()
                val upY = event.y.toInt()
                if (upX in toleranceXRange && upY in toleranceYRange) {
                    v.performClick()

                }
                true
            }
            else -> false
        }
    }
}