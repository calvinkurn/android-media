package com.tokopedia.topads.detail_sheet

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet

import com.elyeproj.loaderviewlibrary.LoaderImageView

/**
 * Author errysuprayogi on 22,October,2019
 */
class ShimmeringRoundedImageView : LoaderImageView {

    private val radius = 8.0f
    private var path: Path? = null
    private var rect: RectF? = null


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        path = Path()
    }

    override fun onDraw(canvas: Canvas) {
        rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        path?.let {
            it.addRoundRect(rect, radius, radius, Path.Direction.CW)
        }
        canvas.clipPath(path!!)
        super.onDraw(canvas)
    }
}
