package com.tokopedia.search.utils
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class TopCropImageView : AppCompatImageView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recomputeImgMatrix()
    }

    override fun setFrame(l: Int, t: Int, r: Int, b: Int): Boolean {
        recomputeImgMatrix()
        return super.setFrame(l, t, r, b)
    }

    private fun init() {
        scaleType = ScaleType.MATRIX
    }

    /**
     * Recompute image matrix so that drawable shall always fit view width
     */
    private fun recomputeImgMatrix() {
        val drawable = drawable ?: return
        val matrix = imageMatrix
        val scale: Float
        val viewWidth = width - paddingLeft - paddingRight
        val drawableWidth = drawable.intrinsicWidth
        scale = viewWidth.toFloat() / drawableWidth.toFloat()
        matrix.setScale(scale, scale)
        imageMatrix = matrix
    }
}
