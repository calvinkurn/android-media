package com.tokopedia.shop.score.performance.presentation.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.shop.score.R
import kotlin.math.max

class PenaltyDotBadge(private val context: Context) : Drawable() {

    private val mBadgePaint: Paint = Paint()
    private var willDraw = false

    companion object {
        private const val DIVIDE_TWO = 2
        private const val PLUS_CENTER_Y = 10
        private const val MINUS_CENTER_X = 20F
        private const val DENSITY_PIXEL_16 = 16
    }

    init {
        mBadgePaint.color = ContextCompat.getColor(
            context.applicationContext,
            com.tokopedia.unifyprinciples.R.color.Unify_RN500
        )
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        if (!willDraw) return

        val dp16 = context.dpToPx(DENSITY_PIXEL_16)
        val width: Float = dp16
        val height: Float = dp16

        val radius = max(width, height) / DIVIDE_TWO / DIVIDE_TWO
        val centerX = (bounds.right - bounds.left).minus(MINUS_CENTER_X)
        val centerY = radius.plus(PLUS_CENTER_Y)
        canvas.drawCircle(centerX, centerY, radius, mBadgePaint)
    }

    fun showBadge(menuItem: MenuItem) {
        val icon = getIcon(menuItem)

        showBadge()
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_dot, this)
    }

    fun removeBadge(menuItem: MenuItem) {
        val icon = getIcon(menuItem)

        removeBadge()
        icon?.mutate()
        icon?.setDrawableByLayerId(R.id.ic_dot, this)
    }

    private fun getIcon(menuItem: MenuItem): LayerDrawable? {
        val menuIcon = menuItem.icon
        val icon: LayerDrawable? = if (menuIcon is LayerDrawable) menuIcon else null
        return icon
    }

    private fun showBadge() {
        willDraw = true
        invalidateSelf()
    }

    private fun removeBadge() {
        willDraw = false
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(cf: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}