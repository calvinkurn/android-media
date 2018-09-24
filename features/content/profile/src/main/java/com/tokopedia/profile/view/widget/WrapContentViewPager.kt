package com.tokopedia.profile.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.component.ticker.TouchViewPager

/**
 * @author by milhamj on 9/21/18.
 */
class WrapContentViewPager : TouchViewPager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        getChildAt(currentItem)?.let {
            it.measure(
                    widthMeasureSpec,
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val height = it.measuredHeight
            super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
            return
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}