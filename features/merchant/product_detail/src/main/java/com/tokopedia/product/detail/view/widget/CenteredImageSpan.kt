package com.tokopedia.product.detail.view.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan

class CenteredImageSpan(
    drawable: Drawable,
    private val lineSpacing: Float
) : ImageSpan(drawable) {

    companion object {
        private const val HALF_DIVIDER = 2f
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
