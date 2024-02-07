package com.tokopedia.product.detail.view.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class CenteredImageSpan(drawable: Drawable) : ImageSpan(drawable) {

    companion object {
        private const val HALF_DIVIDER = 2f
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fontMetricsInt: Paint.FontMetricsInt?
    ): Int {
        val rect = drawable.bounds
        val fm = fontMetricsInt ?: return rect.right
        val fmPaint = paint.fontMetricsInt
        val centerY = fmPaint.ascent + fmPaint.fontHeight.div(HALF_DIVIDER.toInt())
        val halfDrawableHeight = rect.drawableHeight.div(HALF_DIVIDER.toInt())

        fm.ascent = centerY - halfDrawableHeight
        fm.top = fm.ascent
        fm.bottom = centerY + halfDrawableHeight
        fm.descent = fm.bottom

        return rect.right
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
        val drawable = drawable
        val fmPaint = paint.fontMetricsInt
        val centerY = y + fmPaint.descent - fmPaint.fontHeight.div(HALF_DIVIDER)
        val transY = centerY - drawable.drawableHeight.div(HALF_DIVIDER)

        canvas.save()
        canvas.translate(x, transY)
        drawable.draw(canvas)
        canvas.restore()
    }

    private val Paint.FontMetricsInt.fontHeight
        get() = descent - ascent

    private val Rect.drawableHeight
        get() = bottom - top

    private val Drawable.drawableHeight
        get() = bounds.drawableHeight
}
