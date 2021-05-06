package com.tokopedia.floatingwindow.util

import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.hypot

/**
 * Created by jegul on 26/11/20
 */
class FloatingWindowTouchListener(
        view: View,
        private val initialPosition: () -> Point,
        private val positionListener: (x: Int, y: Int) -> Unit
) : View.OnTouchListener {

    private val touchSlop = ViewConfiguration.get(view.context).scaledTouchSlop
    private var pointerStartX = 0
    private var pointerStartY = 0
    private var initialX = 0
    private var initialY = 0
    private var moving = false

    init {
        view.setOnTouchListener(this)
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {

        return when (motionEvent.action) {

            MotionEvent.ACTION_DOWN -> {
                pointerStartX = motionEvent.rawX.toInt()
                pointerStartY = motionEvent.rawY.toInt()
                with(initialPosition()) {
                    initialX = x
                    initialY = y
                }
                moving = false

                true
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = motionEvent.rawX - pointerStartX
                val deltaY = motionEvent.rawY - pointerStartY
                if (moving || hypot(deltaX, deltaY) > touchSlop) {
                    positionListener(initialX + deltaX.toInt(), initialY + deltaY.toInt())
                    moving = true
                }

                true
            }

            MotionEvent.ACTION_UP -> {
                val deltaX = motionEvent.rawX - pointerStartX
                val deltaY = motionEvent.rawY - pointerStartY
                if (hypot(deltaX, deltaY) <= touchSlop) {
                    view.performClick()
                }

                true
            }

            else -> false
        }
    }

}