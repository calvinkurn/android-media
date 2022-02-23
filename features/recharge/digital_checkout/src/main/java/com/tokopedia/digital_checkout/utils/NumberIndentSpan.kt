package com.tokopedia.digital_checkout.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan

/**
 * @author by furqan on 02/24/22
 */
class NumberIndentSpan(
    private val leadWidth: Int,
    private val gapWidth: Int,
    private val index: Int
) : LeadingMarginSpan {

    override fun getLeadingMargin(first: Boolean): Int {
        return leadWidth + gapWidth
    }

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
        l: Layout?
    ) {
        if (first) {
            val orgStyle: Paint.Style = p.style
            p.style = Paint.Style.FILL
            val width: Float = p.measureText(TWO_DIGIT_NUMBER_FOR_MEASUREMENT)
            c.drawText("$index.", (leadWidth + x - width / TWO) * dir, bottom - p.descent(), p)
            p.style = orgStyle
        }
    }

    companion object {
        private const val TWO_DIGIT_NUMBER_FOR_MEASUREMENT = "10."
        private const val TWO = 2
    }
}