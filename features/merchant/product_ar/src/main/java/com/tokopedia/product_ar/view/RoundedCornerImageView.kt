package com.tokopedia.product_ar.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product_ar.R

class ProductRoundedBorderImageView : AppCompatImageView {

    private val DEFAULT_RADIUS = 4f.toPx()
    var setSelected: Boolean = false
        set(value) {
            val temp = field
            field = value

            if (temp != value) {
                invalidate()
            }
        }

    private var radius = DEFAULT_RADIUS
    private var path = Path()
    private var rect: RectF = RectF()
    private var mBorderPaint = Paint()

    constructor(context: Context) : super(context) {
        setup(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(context, attrs)
    }

    private fun setup(context: Context, attrs: AttributeSet?) {
        getDeclaredRadius(context, attrs)
        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.color = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        mBorderPaint.strokeWidth = 10F
    }

    private fun getDeclaredRadius(context: Context?, attrs: AttributeSet?) {
        val ta = context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.ProductRoundedBorderImageView,
                0,
                0
        )
        radius = (ta?.getDimensionPixelSize(R.styleable.ProductRoundedBorderImageView_borderRadius, DEFAULT_RADIUS.toInt())
                ?: 0).toFloat()
        ta?.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        rect.right = measuredWidth.toFloat()
        rect.bottom = measuredHeight.toFloat()

        path.addRoundRect(rect, radius, radius, Path.Direction.CW)

        canvas?.clipPath(path)
        super.onDraw(canvas)

        if (setSelected) {
            canvas?.drawRoundRect(rect, radius, radius, mBorderPaint)
        }
    }
}