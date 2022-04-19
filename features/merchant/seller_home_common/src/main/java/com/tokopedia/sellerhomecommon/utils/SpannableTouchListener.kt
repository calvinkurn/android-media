package com.tokopedia.sellerhomecommon.utils

import android.text.Layout
import android.text.Selection
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

/**
 * Custom OnTouchListener to support clickable url span
 *
 * This is used as a substitute for using [LinkMovementMethod.getInstance] in movementMethod for enabling click event
 * since using that could remove ellipsize in our text
 */
internal class SpannableTouchListener(var spannable: Spannable) : View.OnTouchListener {
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val action = event.action
        if (v !is TextView) {
            return false
        }
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= v.totalPaddingLeft
            y -= v.totalPaddingTop
            x += v.scrollX
            y += v.scrollY
            val layout: Layout = v.layout
            val line: Int = layout.getLineForVertical(y)
            val off: Int = layout.getOffsetForHorizontal(line, x.toFloat())
            val link = spannable.getSpans(off, off, ClickableSpan::class.java)
            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    v.performClick()
                    link[0].onClick(v)
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(spannable,
                            spannable.getSpanStart(link[0]),
                            spannable.getSpanEnd(link[0]))
                }
                return true
            } else {
                Selection.removeSelection(spannable)
            }
        }
        return false
    }
}