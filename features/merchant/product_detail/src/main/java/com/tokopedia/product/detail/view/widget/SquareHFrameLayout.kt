package com.tokopedia.product.detail.view.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.min

class SquareHFrameLayout : FrameLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minimumMeasureSpec = min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(minimumMeasureSpec, minimumMeasureSpec)
    }

}
