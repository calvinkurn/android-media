package com.tokopedia.home_account.account_settings.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.FontMetricsInt
import android.graphics.RectF
import android.text.style.ReplacementSpan
import android.util.TypedValue
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

class TagRoundedSpan(
    private val context: Context,
    cornerRadius: Int,
    @ColorRes backgroundColor: Int,
    @ColorRes textColor: Int
) : ReplacementSpan() {
    private val cornerRadius: Float
    private val leftPadding: Float
    private val rightPadding: Float
    private val backgroundColor: Int
    private val textColor: Int

    init {
        this.backgroundColor = ContextCompat.getColor(context, backgroundColor)
        this.textColor = ContextCompat.getColor(context, textColor)
        this.cornerRadius = toPixel(cornerRadius.toFloat())
        leftPadding = toPixel(5f)
        rightPadding = toPixel(5f)
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: FontMetricsInt?
    ): Int {
        return Math.round(paint.measureText(text, start, end))
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val emptySpace = (bottom - y) / 2f
        val bgTop = top + emptySpace
        val bgBottom = bottom - emptySpace
        val bgRight = x + measureText(paint, text, start, end) + rightPadding + leftPadding
        val yText = y - emptySpace
        val xText = x + leftPadding
        val rect = RectF(x, bgTop, bgRight, bgBottom)
        paint.color = backgroundColor
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
        paint.color = textColor
        canvas.drawText(text, start, end, xText, yText, paint)
    }

    private fun toPixel(dip: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            context.resources.displayMetrics
        )
    }

    private fun measureText(paint: Paint, text: CharSequence, start: Int, end: Int): Float {
        return paint.measureText(text, start, end)
    }
}