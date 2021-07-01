package com.tokopedia.product_bundle.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product_bundle.R

class RoundedCornerImageView : AppCompatImageView {
    companion object {
        private const val DEFAULT_RADIUS = 8f
    }

    private var radius = 4f.toPx()
    private var path = Path()
    private var rect: RectF = RectF()

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
        radius = getDeclaredRadius(context, attrs)
    }

    private fun getDeclaredRadius(context: Context?, attrs: AttributeSet?): Float {
        if (attrs == null) return DEFAULT_RADIUS.toPx()
        val ta = context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.rounded_corner_imageView,
                0,
                0
        ) ?: return DEFAULT_RADIUS.toPx()
        val defValue = DEFAULT_RADIUS.toPx().toInt()
        val attrRadius = ta.getDimensionPixelSize(R.styleable.rounded_corner_imageView_border_radius, defValue)
        ta.recycle()
        return attrRadius.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        rect.right = measuredWidth.toFloat()
        rect.bottom = measuredHeight.toFloat()
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }
}