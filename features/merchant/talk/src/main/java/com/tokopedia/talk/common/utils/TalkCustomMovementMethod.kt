package com.tokopedia.talk.common.utils

import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView
import com.tokopedia.unifyprinciples.Typography

fun Typography.setCustomMovementMethod(linkAction: (String) -> Boolean) {
    this.movementMethod = object : LinkMovementMethod() {
        override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
            val action = event.action

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x
                var y = event.y.toInt()

                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop

                x += widget.scrollX
                y += widget.scrollY

                val layout = widget.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x)

                val link = buffer.getSpans(off, off, URLSpan::class.java)
                if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                    return linkAction.invoke(link.first().url.toString())
                }
            }
            return super.onTouchEvent(widget, buffer, event);
        }
    }
}