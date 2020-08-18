package com.tokopedia.shop.pageheader.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

class RoundedBackgroundColorSpan(
        private val backgroundColor: Int,
        private val padding: Float,
        private val radius: Float
) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return (padding + paint.measureText(text.subSequence(start, end).toString()) + padding).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val width = paint.measureText(text.subSequence(start, end).toString())
        val rect = RectF(x, top.toFloat(), x + width + 2 * padding, bottom.toFloat())
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, radius, radius, paint)
        paint.color = Color.WHITE
        canvas.drawText(text, start, end, x + padding, y.toFloat(), paint)

    }
}