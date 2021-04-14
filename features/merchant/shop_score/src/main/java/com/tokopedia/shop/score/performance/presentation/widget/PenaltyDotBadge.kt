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

    init {
        mBadgePaint.color = ContextCompat.getColor(context.applicationContext, com.tokopedia.unifyprinciples.R.color.Unify_R600)
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        if (!willDraw) return

        val dp16 = context.dpToPx(16)
        val width: Float = dp16
        val height: Float = dp16

        val radius = max(width, height) / 2 / 2
        val centerX = (bounds.right - bounds.left).minus(20f)
        val centerY = radius.plus(10)
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