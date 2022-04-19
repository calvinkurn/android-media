package com.tokopedia.flight.filter.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText


/**
 * @author by furqan on 20/02/2020
 */
class FlightPriceEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    var originalLeftPadding: Int = -1

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculatePrefix()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val mPaint = Paint()
        mPaint.color = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        mPaint.typeface = com.tokopedia.unifyprinciples.getTypeface(context, "NunitoSansExtraBold.ttf")
        mPaint.textSize = resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl3).toFloat()

        canvas.drawText(tag as String, originalLeftPadding.toFloat(), getLineBounds(0, null).toFloat(), mPaint)
    }

    private fun calculatePrefix() {
        if (originalLeftPadding == -1) {
            val prefix = tag as String
            val widths = FloatArray(prefix.length)
            paint.getTextWidths(prefix, widths)
            var textWidth = 8f
            for (w in widths) {
                textWidth += w
            }
            originalLeftPadding = compoundPaddingLeft
            setPadding((textWidth + originalLeftPadding).toInt(),
                    paddingRight, paddingTop,
                    paddingBottom)
        }
    }

}