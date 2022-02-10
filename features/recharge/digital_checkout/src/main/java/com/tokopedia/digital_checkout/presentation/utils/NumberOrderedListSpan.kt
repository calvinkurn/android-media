package com.tokopedia.digital_checkout.presentation.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan

class NumberOrderedListSpan(
    private val width: Int,
    private val leadingText: String
) : LeadingMarginSpan {

    override fun getLeadingMargin(first: Boolean): Int = width

    override fun drawLeadingMargin(
        c: Canvas?,
        p: Paint?,
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
        // Check if we're at the start of the span
        val spanStart = (text as Spanned).getSpanStart(this)
        val isFirstCharacter = spanStart == start

        // If so, draw the text in the leading span
        if (isFirstCharacter && p != null) {
            c?.drawText(leadingText, x.toFloat(), baseline.toFloat(), p)
        }
    }
}