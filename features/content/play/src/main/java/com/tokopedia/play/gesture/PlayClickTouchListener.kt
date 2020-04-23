package com.tokopedia.play.gesture

import android.view.MotionEvent
import android.view.View

/**
 * Created by jegul on 23/04/20
 */
class PlayClickTouchListener(private val toleration: Int) : View.OnTouchListener {

    private var currentX = -1
    private var currentY = -1

    private val tolerationXRange: IntRange
        get() = currentX - toleration .. currentX + toleration

    private val tolerationYRange: IntRange
        get() = currentY - toleration .. currentY + toleration

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
                if (upX in tolerationXRange && upY in tolerationYRange) {
                    v.performClick()

                }
                true
            }
            else -> false
        }
    }
}