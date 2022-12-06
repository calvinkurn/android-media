package com.tokopedia.sellerorder.common.listener

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import com.tokopedia.kotlin.extensions.orFalse

@SuppressLint("ClickableViewAccessibility")
class SingleTapListener(context: Context, action: (event: MotionEvent?) -> Boolean) {

    private val gestureListener by lazy { createGestureListener(action) }
    private val gestureDetector by lazy { createGestureDetector(context, gestureListener) }
    private val touchListener by lazy { createTouchListener(gestureDetector) }

    private fun createGestureListener(
        action: (event: MotionEvent?) -> Boolean
    ): GestureDetector.SimpleOnGestureListener {
        return object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return action(e)
            }
        }
    }
    
    private fun createGestureDetector(
        context: Context,
        gestureListener: GestureDetector.SimpleOnGestureListener
    ) = GestureDetectorCompat(context, gestureListener)

    private fun createTouchListener(
        gestureDetector: GestureDetectorCompat
    ) = View.OnTouchListener { _, event ->
        gestureDetector.onTouchEvent(event).orFalse()
    }

    fun attachListener(view: View) {
        view.setOnTouchListener(touchListener)
    }
}
