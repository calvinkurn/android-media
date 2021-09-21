package com.tokopedia.product.detail.common.data.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.product.detail.common.R

/**
 * Created by Yehezkiel on 08/03/20
 */
class ProductRoundedImageView : AppCompatImageView {

    private val DEFAULT_RADIUS = 4f.toPx()

    private var radius = DEFAULT_RADIUS
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
        if (attrs == null) return DEFAULT_RADIUS
        val ta = context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.ProductRoundedImageView,
                0,
                0
        ) ?: return DEFAULT_RADIUS
        val attrRadius = ta.getDimensionPixelSize(R.styleable.ProductRoundedImageView_borderRadius, DEFAULT_RADIUS.toInt())
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