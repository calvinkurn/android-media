package com.tokopedia.withdraw.saldowithdrawal.helper

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.text.Layout
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px


class BulletPointSpan(@Px private val gapWidth: Int,@Px private val radius: Float,
                      @ColorInt private val color: Int) : LeadingMarginSpan {
    private lateinit var bulletPath : Path


    override fun getLeadingMargin(first: Boolean): Int {
        return (2 * radius + 2 * gapWidth).toInt()
    }

    override fun drawLeadingMargin(canvas: Canvas, paint: Paint, x: Int, dir: Int,
                          top: Int, baseline: Int, bottom: Int,
                          text: CharSequence, start: Int, end: Int,
                          first: Boolean, l: Layout?) {
        if ((text as Spanned).getSpanStart(this) === start) {
            val style: Paint.Style = paint.style
            val oldColor: Int = paint.color
            paint.color = color
            paint.style = Paint.Style.FILL
            val y = (top + bottom) / 2f
            if (canvas.isHardwareAccelerated) {
                if (!::bulletPath.isInitialized) {
                    bulletPath = Path()
                    bulletPath.addCircle(0.0f, 0.0f, radius, Path.Direction.CW)
                }
                canvas.save()
                canvas.translate(gapWidth + x + dir * radius, y)
                canvas.drawPath(bulletPath, paint)
                canvas.restore()
            } else {
                canvas.drawCircle(gapWidth + x + dir * radius, y, radius, paint)
            }
            paint.color = oldColor
            paint.style = style
        }
    }


}