package com.tokopedia.shop.common.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.shop.R

class ShopPageCountDrawable(context: Context) : Drawable() {
    private var badgePaint: Paint? = null
    private var textPaint: Paint? = null
    private val txtRect = Rect()
    private var count = ""
    private var willDraw = false
    private fun createTextPaint(context: Context) {
        textPaint = Paint()
        textPaint?.color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        textPaint?.typeface = Typeface.DEFAULT
        textPaint?.textSize = context.resources.getDimension(R.dimen.sp_8)
        textPaint?.isAntiAlias = true
        textPaint?.textAlign = Paint.Align.CENTER
    }

    private fun createBadgePaint(context: Context) {
        badgePaint = Paint()
        badgePaint?.color = ContextCompat.getColor(context.applicationContext, com.tokopedia.unifyprinciples.R.color.Unify_Y500)
        badgePaint?.isAntiAlias = true
        badgePaint?.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        if (!willDraw) {
            return
        }
        val bounds = bounds
        val width = (bounds.right - bounds.left).toFloat()
        val height = (bounds.bottom - bounds.top).toFloat()

        // Position the badge in the top-right quadrant of the icon.

        /*Using Math.max rather than Math.min */
        val radius = Math.max(width, height) / 2 / 2
        val centerX = width - radius - 1
        val centerY = radius - 3
        badgePaint?.let{
            if (count.length <= 2) {
                // Draw badge circle.
                canvas.drawCircle(centerX, centerY, (radius + 5.5).toFloat(), it)
            } else {
                canvas.drawCircle(centerX, centerY, (radius + 6.5).toFloat(), it)
            }
        }
        // Draw badge count text inside the circle.
        textPaint!!.getTextBounds(count, 0, count.length, txtRect)
        val textHeight = (txtRect.bottom - txtRect.top).toFloat()
        val textY = centerY + textHeight / 2f
        if (count.length > 2) canvas.drawText("99+", centerX, textY, textPaint!!) else canvas.drawText(count, centerX, textY, textPaint!!)
    }

    /*
    Sets the count (i.e notifications) to display.
     */
    fun setCount(count: String) {
        this.count = count

        // Only draw a badge if there are notifications.
        willDraw = !count.equals("0", ignoreCase = true)
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
        // do nothing
    }

    override fun setColorFilter(cf: ColorFilter?) {
        // do nothing
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    init {
        createBadgePaint(context)
        createTextPaint(context)
    }
}