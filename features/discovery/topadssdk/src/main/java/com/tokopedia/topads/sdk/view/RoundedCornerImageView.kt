package com.tokopedia.topads.sdk.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView

class RoundedCornerImageView : ImageView {
    private val radius = 5.0f
    private var path: Path? = null
    private var rect: RectF? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    private fun init() {
        path = Path()
    }

    override fun onDraw(canvas: Canvas) {
        rect = RectF(0F, 0F, this.width.toFloat(), this.height.toFloat())
        rect?.let { path?.addRoundRect(it, radius, radius, Path.Direction.CW) }
        path?.let { canvas.clipPath(it) }
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
