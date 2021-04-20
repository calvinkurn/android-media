package com.tokopedia.activation.util

import android.text.Layout
import android.text.Spanned
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class ActivationPageTouchListener(private val goToInfoPage: (String) -> Boolean) : View.OnTouchListener {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val widget = v as TextView
        val text: Any = widget.text
        if (text is Spanned) {
            val action = event.action
            if (action == MotionEvent.ACTION_UP
                    || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()
                x -= widget.totalPaddingLeft
                y -= widget.totalPaddingTop
                x += widget.scrollX
                y += widget.scrollY
                val layout: Layout = widget.layout
                val line: Int = layout.getLineForVertical(y)
                val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())
                val link = text.getSpans(off, off, URLSpan::class.java)
                if (link.isNotEmpty() && action == MotionEvent.ACTION_UP) {
                    return goToInfoPage(link.first().url.toString())
                }
            }
        }
        return true
    }
}