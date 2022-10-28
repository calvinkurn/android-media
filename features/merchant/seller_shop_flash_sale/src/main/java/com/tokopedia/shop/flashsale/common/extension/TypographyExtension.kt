package com.tokopedia.shop.flashsale.common.extension

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import com.tokopedia.unifyprinciples.Typography

private const val SPAN_NUMBER = 1
private const val SPAN_FLAGS = 0
private const val LEAD_WIDTH = 15
private const val GAP_WIDTH = 30
private const val TWO = 2

fun Typography.setNumberedText(text: List<String>) {
    val content = SpannableStringBuilder()

    text.forEachIndexed { index, text ->
        val contentStart = content.length
        content.appendLine(text)
        content.setSpan(
            NumberIndentSpan(index + SPAN_NUMBER),
            contentStart,
            content.length,
            SPAN_FLAGS
        )
    }

    this.text = content
}

fun Typography.strikethrough() {
    this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

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