package com.tokopedia.logisticseller.ui.requestpickup.util

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan

private const val LEAD_WIDTH = 30
private const val GAP_WIDTH = 30
private const val TWO = 2
class NumberIndentSpan(
    private val number: Int
) : LeadingMarginSpan {
    override fun getLeadingMargin(first: Boolean): Int {
        return LEAD_WIDTH + GAP_WIDTH
    }

    override fun drawLeadingMargin(
        canvas: Canvas,
        paint: Paint,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        t: CharSequence?,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout?
    ) {
        if (first) {
            val text = "$number."
            val width = paint.measureText(text)

            val yPosition = baseline.toFloat()
            val xPosition = (LEAD_WIDTH + x - width / TWO) * dir

            canvas.drawText(text, xPosition, yPosition, paint)
        }
    }
}
