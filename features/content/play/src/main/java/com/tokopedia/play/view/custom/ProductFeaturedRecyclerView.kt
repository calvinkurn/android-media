package com.tokopedia.play.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Shader
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ProductFeaturedRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val fadeColors = intArrayOf(
        Color.BLACK,
        Color.TRANSPARENT
    )

    private val mFadingEdgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    }

    private var mEndBounds: Int = 0
    private val mEndGradientRect = Rect(0, 0, 0, 0)

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        if (visibility == View.GONE || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return super.drawChild(canvas, child, drawingTime)
        }

        val count = canvas.saveLayer(
            0.0f, 0.0f,
            width.toFloat(), height.toFloat(), null
        )
        val drawChild = super.drawChild(canvas, child, drawingTime)
        canvas.drawRect(
            mEndGradientRect,
            mFadingEdgePaint
        )

        canvas.restoreToCount(count)
        return drawChild
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupEndGradientRect(mEndBounds)
    }

    fun setFadingEndBounds(endBounds: Int) {
        if (mEndBounds == endBounds) return

        mEndBounds = endBounds
        setupEndGradientRect(endBounds)
    }

    private fun setupEndGradientRect(endBounds: Int) {
        val startGradient = width - START_GRADIENT * endBounds
        mFadingEdgePaint.shader = getHorizontalGradientShader(
            startGradient.toFloat(),
            endBounds.toFloat()
        )
        invalidateEndGradientRect(startGradient)
        invalidate()
    }

    private fun invalidateEndGradientRect(start: Int = mEndGradientRect.left) {
        mEndGradientRect.set(start, 0, width, height)
    }

    private fun getHorizontalGradientShader(
        startGradient: Float,
        endBounds: Float
    ): LinearGradient {
        val endGradient = width - endBounds
        return LinearGradient(
            startGradient,
            0f,
            endGradient,
            0f,
            fadeColors,
            null,
            Shader.TileMode.CLAMP
        )
    }

    companion object {
        private const val START_GRADIENT = 3
    }
}