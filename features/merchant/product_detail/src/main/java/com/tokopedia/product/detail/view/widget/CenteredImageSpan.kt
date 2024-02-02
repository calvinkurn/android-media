package com.tokopedia.product.detail.view.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import com.tokopedia.kotlin.extensions.view.ZERO

class CenteredImageSpan(
    drawable: Drawable,
    private val lineSpacing: Float = Float.ZERO
) : ImageSpan(drawable) {

    companion object {
        private const val HALF_DIVIDER = 2f
    }

    override fun getSize(paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        val rect = drawable.bounds
        val pfm = paint.fontMetricsInt

        if (fm != null) {
            val halfDivider = HALF_DIVIDER.toInt()
            val halfBound = rect.height().div(halfDivider)
            val pfmAscent = pfm.ascent.div(halfDivider)
            fm.ascent = -halfBound + pfmAscent
            fm.descent = Int.ZERO.coerceAtLeast(minimumValue = halfBound + pfmAscent)
            fm.top = fm.ascent
            fm.bottom = fm.descent
        }

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
        canvas.save()

        val halfHeight = (bottom - top).div(HALF_DIVIDER)
        val halfBoundHeight = drawable.bounds.height().div(HALF_DIVIDER)
        val halfLineSpacing = lineSpacing.div(HALF_DIVIDER)
        val transY = halfHeight - halfBoundHeight - halfLineSpacing

        canvas.translate(x, transY)
        drawable.draw(canvas)
        canvas.restore()
    }
}
