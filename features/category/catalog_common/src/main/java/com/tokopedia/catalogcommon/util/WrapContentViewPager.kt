package com.tokopedia.catalogcommon.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

class WrapContentViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpecNew = heightMeasureSpec
        val child = getChildAt(currentItem)
        child?.let {
            it.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val height = it.measuredHeight
            heightMeasureSpecNew = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpecNew)
    }
}
