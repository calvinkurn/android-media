package com.tokopedia.shop.flash_sale.common.extension

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import com.tokopedia.unifyprinciples.Typography

fun Typography.setNumberedText(text: List<String>, numberGap: Int) {
    val content = SpannableStringBuilder()

    text.forEachIndexed { index, text ->
        val contentStart = content.length
        content.appendLine(text)
        content.setSpan(
            NumberIndentSpan(index + 1, gapWidth = numberGap),
            contentStart,
            content.length,
            0
        )
    }

    this.text = content
}

class NumberIndentSpan(
    private val number: Int,
    private val leadWidth: Int = 15,
    private val gapWidth: Int = 30,
) : LeadingMarginSpan {
    override fun getLeadingMargin(first: Boolean): Int {
        return leadWidth + gapWidth
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
            val xPosition = (leadWidth + x - width / 2) * dir

            canvas.drawText(text, xPosition, yPosition, paint)
        }
    }
}