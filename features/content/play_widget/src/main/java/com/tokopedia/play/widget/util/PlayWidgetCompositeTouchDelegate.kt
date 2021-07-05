package com.tokopedia.play.widget.util

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View

/**
 * Created by jegul on 16/11/20
 */
class PlayWidgetCompositeTouchDelegate(view: View) : TouchDelegate(emptyRect, view) {
    private val delegates = mutableListOf<TouchDelegate>()

    fun addDelegate(delegate: TouchDelegate?) {
        if (delegate != null) {
            delegates.add(delegate)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var res = false
        val x = event.x
        val y = event.y
        for (delegate in delegates) {
            event.setLocation(x, y)
            res = delegate.onTouchEvent(event) || res
        }
        return res
    }

    companion object {
        private val emptyRect: Rect = Rect()
    }
}
