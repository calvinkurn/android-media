package com.tokopedia.product.detail.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.detail.common.R

/*
    +-----------------------------------+
    |                                   |
    |      Your Custom layout Here      |
    |                                   |
    +-----------------------------------+
 */
class BorderedConstraintLayout : ConstraintLayout {
    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    init {
        setWillNotDraw(false)
    }

    private var mBorderPaint = Paint()
    private var rect: RectF = RectF()
    private var radius = 4f.toPx()

    private fun setup(context: Context, attrs: AttributeSet?) {
        getDeclaredRadius(context, attrs)
        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.color = ContextCompat.getColor(this.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN200)
        mBorderPaint.strokeWidth = 5F
    }

    private fun getDeclaredRadius(context: Context?, attrs: AttributeSet?) {
        val ta = context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.ProductRoundedImageView,
                0,
                0
        )
        radius = (ta?.getDimensionPixelSize(R.styleable.ProductRoundedImageView_borderRadius, radius.toInt())
                ?: 0).toFloat()
        ta?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        rect.top = this.paddingTop.toFloat()
        rect.left = this.paddingLeft.toFloat()
        rect.right = measuredWidth.toFloat() - this.paddingStart
        rect.bottom = measuredHeight.toFloat() - this.paddingBottom

        canvas?.drawRoundRect(rect, radius, radius, mBorderPaint)
    }
}