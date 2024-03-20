package com.tokopedia.content.common.util

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View

class CompositeTouchDelegate(view: View) : TouchDelegate(emptyRect, view) {
    private val delegates = mutableMapOf<Any, TouchDelegate>()

    fun addDelegate(key: Any, delegate: TouchDelegate?) {
        if (delegate == null) return
        if (delegates[key] != null) return

        delegates[key] = delegate
    }

    fun remove(key: Any) {
        delegates.remove(key)
    }

    fun removeDelegate(delegate: TouchDelegate) {
        delegates.entries.removeAll { it.value == delegate }
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
            res = delegate.value.onTouchEvent(event) || res
        }
        return res
    }

    companion object {
        private val emptyRect: Rect = Rect()
    }
}
