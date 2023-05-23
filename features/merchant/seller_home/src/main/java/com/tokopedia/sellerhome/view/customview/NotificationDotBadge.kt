package com.tokopedia.sellerhome.view.customview

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
import com.tokopedia.sellerhome.R
import kotlin.math.max

/**
 * Created By @ilhamsuaib on 2020-03-04
 */

class NotificationDotBadge(private val context: Context) : Drawable() {

    companion object {
        private const val DIMEN_16 = 16
        private const val RADIUS = 2
        private const val CENTER_X = 12f
        private const val CENTER_Y = 10
    }

    private val mBadgePaint: Paint = Paint()
    private var willDraw = false

    init {
        mBadgePaint.color = ContextCompat.getColor(context.applicationContext, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        mBadgePaint.isAntiAlias = true
        mBadgePaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas) {
        if (!willDraw) return

        val dp16 = context.dpToPx(DIMEN_16)
        val width: Float = dp16
        val height: Float = dp16

        val radius = max(width, height) / RADIUS / RADIUS
        val centerX = (bounds.right - bounds.left).minus(CENTER_X)
        val centerY = radius.plus(CENTER_Y)
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
        return menuIcon as? LayerDrawable
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