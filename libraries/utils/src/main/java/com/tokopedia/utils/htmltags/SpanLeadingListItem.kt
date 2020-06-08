package com.tokopedia.utils.htmltags

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan

class SpanLeadingListItem(
        private val maxMargin: Int,
        private val mark: String) : LeadingMarginSpan {
    override fun drawLeadingMargin(
            c: Canvas,
            p: Paint,
            x: Int,
            dir: Int,
            top: Int,
            baseline: Int,
            bottom: Int,
            text: CharSequence?,
            start: Int,
            end: Int,
            first: Boolean,
            layout: Layout?
    ) {
        val startSpan = (text as Spanned).getSpanStart(this)
        val isFirstChar = startSpan == start

        if (isFirstChar) {
            c.drawText(mark, 0f, baseline.toFloat(), p)
        }
    }

    override fun getLeadingMargin(first: Boolean) = maxMargin
}