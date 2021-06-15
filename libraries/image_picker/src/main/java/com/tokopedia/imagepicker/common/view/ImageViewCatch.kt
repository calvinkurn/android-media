package com.tokopedia.imagepicker.common.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * Class Image View To handle too large draw bitmap especially in Xiaomi android 11.
 */
class ImageViewCatch : AppCompatImageView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        try {
            super.onDraw(canvas)
        } catch (e:Throwable) {
            // nothing to draw
        }
    }
}
