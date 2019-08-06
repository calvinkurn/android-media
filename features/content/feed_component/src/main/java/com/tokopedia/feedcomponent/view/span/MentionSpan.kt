package com.tokopedia.feedcomponent.view.span

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.text.style.ReplacementSpan

/**
 * Created by jegul on 2019-08-05.
 */

class MentionSpan(
        @ColorInt private val color: Int,
        val fullText: String,
        val userId: String,
        val fullName: String
) : ReplacementSpan() {

    val length = fullName.length

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, p4: Paint.FontMetricsInt?): Int {
        return Math.round(measureText(paint, text, start, end))
    }

    override fun draw(
            canvas: Canvas,
            text: CharSequence?,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
    ) {
        text?.let {
            paint.color = color
            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText(it, start, end, x, y.toFloat(), paint)
        }
    }

    private fun measureText(paint: Paint, text: CharSequence?, start: Int, end: Int): Float {
        paint.typeface = Typeface.DEFAULT_BOLD
        return paint.measureText(text, start, end)
    }
}