package com.tokopedia.contactus.inboxtickets.view.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan

/**
 * @param mBackgroundColor color value, not res id
 * @param mTextSize        in pixels
 */
class RoundedBackgroundSpan constructor(private val mBackgroundColor: Int,
            private val mTextColor: Int,
            private val mTextSize: Float,
            private val PADDING_X: Float,
            private val PADDING_Y: Float,
            private val MAGIC_NUMBER: Float,
            private val textHeightWrap: Float) : ReplacementSpan() {

    override fun draw(canvas: Canvas, text: CharSequence,
                      start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, mPaint: Paint) {
        var paint = mPaint
        paint = Paint(paint) // make a copy for not editing the referenced paint
        paint.textSize = mTextSize
        // Draw the rounded background
        paint.color = mBackgroundColor
        val tagBottom = top + textHeightWrap + PADDING_Y + mTextSize + PADDING_Y + textHeightWrap
        val tagRight = x + getTagWidth(text, start, end, paint)
        val rect = RectF(x, top.toFloat(), tagRight, tagBottom)
        canvas.drawRoundRect(rect, CORNER_RADIUS.toFloat(), CORNER_RADIUS.toFloat(), paint)
        // Draw the text
        paint.color = mTextColor
        canvas.drawText(text, start, end, x + PADDING_X, tagBottom - PADDING_Y - textHeightWrap - MAGIC_NUMBER, paint)
    }

    private fun getTagWidth(text: CharSequence, start: Int, end: Int, paint: Paint): Int {
        return Math.round(PADDING_X + paint.measureText(text.subSequence(start, end).toString()) + PADDING_X)
    }

    override fun getSize(mPaint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        var paint = mPaint
        paint = Paint(paint) // make a copy for not editing the referenced paint
        paint.textSize = mTextSize
        return getTagWidth(text, start, end, paint)
    }

    companion object {
        private const val CORNER_RADIUS = 15
    }

}
