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

    private var mSize: Int = 0
    private var mShouldFade: Boolean = false
    private val mEndGradientRect = Rect(0, 0, 0, 0)

    override fun drawChild(canvas: Canvas, child: View?, drawingTime: Long): Boolean {
        if (visibility == View.GONE || !mShouldFade) {
            return super.drawChild(canvas, child, drawingTime)
        }

        mFadingEdgePaint.shader = getHorizontalGradientShader(
            (width - mSize).toFloat(), width.toFloat()
        )
        mEndGradientRect.set(
            width - mSize, 0, width + mSize, height
        )
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

    fun setFadingEndWidth(size: Int) {
        if (mSize == size) return

        mSize = size
        if (mShouldFade) invalidate()
    }

    fun setShouldFade(shouldFade: Boolean) {
        if (mShouldFade == shouldFade) return

        mShouldFade = shouldFade
        invalidate()
    }

    private fun getHorizontalGradientShader(
        x0: Float = 0f,
        x1: Float = 0f,
    ): LinearGradient {
        return LinearGradient(
            x0,
            0f,
            x1,
            0f,
            fadeColors,
            null,
            Shader.TileMode.CLAMP
        )
    }
}