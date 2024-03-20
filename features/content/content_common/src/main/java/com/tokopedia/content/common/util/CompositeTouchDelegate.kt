package com.tokopedia.content.common.util

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View

class CompositeTouchDelegate(view: View) : TouchDelegate(emptyRect, view) {
    private val delegates = mutableListOf<TouchDelegate>()

    fun addDelegate(delegate: TouchDelegate?) {
        if (delegate != null && !delegates.contains(delegate)) {
            delegates.add(delegate)
        }
    }

    fun removeDelegate(delegate: TouchDelegate) {
        delegates.remove(delegate)
    }

    fun removeAll() {
        delegates.clear()
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
