package com.tokopedia.sellerhome.view.widget.toolbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.sellerhome.R
import kotlin.math.max

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class CountDrawable(private val context: Context) : Drawable() {

    private val mBadgePaint: Paint = Paint()
    private var willDraw = false

    init {
        mBadgePaint.color = ContextCompat.getColor(context.applicationContext, R.color.green_300)
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        if (!willDraw) return

        val dp8 = context.dpToPx(8)
        val width: Float = dp8
        val height: Float = dp8

        val radius = max(width, height) / 2 / 2
        val centerX = width - radius
        val centerY = width - radius
        canvas.drawCircle(centerX, centerY, (radius + 4f), mBadgePaint)
    }

    fun showBadge() {
        willDraw = true
        invalidateSelf()
    }

    fun removeBadge() {
        willDraw = false
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) { // do nothing
    }

    override fun setColorFilter(cf: ColorFilter?) { // do nothing
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}