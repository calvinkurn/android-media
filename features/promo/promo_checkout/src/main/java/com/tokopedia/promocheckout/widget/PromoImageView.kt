package com.tokopedia.promocheckout.widget

import android.content.Context
import android.util.AttributeSet

/**
 * @author anggaprasetiyo on 08/01/18.
 */

class PromoImageView : android.support.v7.widget.AppCompatImageView {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        setMeasuredDimension(width, width * 213 / 640)
    }
}
