package com.tokopedia.feedplus.presentation.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by kenny.hadisaputra on 18/07/23
 */
class FeedCaptionFadingLayout : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val fadeColors = intArrayOf(
        Color.BLACK,
        Color.TRANSPARENT
    )

    private val mTopFadingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }
    private val mBottomFadingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    private var mTopBounds: Int = 0
    private var mBottomBounds: Int = 0

    private val mTopGradientRect = Rect(0, 0, 0, 0)
    private val mBottomGradientRect = Rect(0, 0, 0, 0)

    override fun dispatchDraw(canvas: Canvas) {
        if (visibility == GONE) {
            super.dispatchDraw(canvas)
        }

        val count = canvas.saveLayer(
            0.0f, 0.0f,
            width.toFloat(), height.toFloat(), null
        )

        super.dispatchDraw(canvas)

        canvas.drawRect(
            mTopGradientRect,
            mTopFadingPaint,
        )

        canvas.drawRect(
            mBottomGradientRect,
            mBottomFadingPaint,
        )

        canvas.restoreToCount(count)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupTopGradientRect(mTopBounds)
        setupBottomGradientRect(mBottomBounds)
    }

    fun setTopFadingEdgeBounds(bounds: Int) {
        if (mTopBounds == bounds) return

        mTopBounds = bounds
        setupTopGradientRect(bounds)
    }

    fun setBottomFadingEdgeBounds(bounds: Int) {
        if (mBottomBounds == bounds) return

        mBottomBounds = bounds
        setupBottomGradientRect(bounds)
    }

    private fun setupTopGradientRect(topBounds: Int) {
        val startGradient = START_GRADIENT_MULTIPLIER * topBounds
        mTopFadingPaint.shader = getVerticalGradientShader(
            startGradient.toFloat(),
            0F
        )
        invalidateVerticalGradientRect(mTopGradientRect, startGradient, 0)
        invalidate()
    }

    private fun setupBottomGradientRect(bottomBounds: Int) {
        val startGradient = height - START_GRADIENT_MULTIPLIER * bottomBounds
        mBottomFadingPaint.shader = getVerticalGradientShader(
            startGradient.toFloat(),
            height.toFloat()
        )
        invalidateVerticalGradientRect(mBottomGradientRect, startGradient, height)
        invalidate()
    }

    private fun invalidateVerticalGradientRect(rect: Rect, startY: Int, endY: Int) {
        rect.set(0, startY, width, endY)
    }

    private fun getVerticalGradientShader(
        startY: Float,
        endY: Float,
    ): LinearGradient {
        return LinearGradient(
            0f,
            startY,
            0f,
            endY,
            fadeColors,
            null,
            Shader.TileMode.CLAMP
        )
    }

    companion object {
        private const val START_GRADIENT_MULTIPLIER = 3
    }
}
