package com.tokopedia.affiliatecommon.view.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.abstraction.base.view.widget.TouchViewPager

/**
 * @author by milhamj on 9/21/18.
 */
class WrapContentViewPager : TouchViewPager {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        var height = 0
//        for (i in 0 until childCount) {
//            val child = getChildAt(i)
//            child.measure(
//                    widthMeasureSpec,
//                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//            )
//            val h = child.measuredHeight
//            if (h > height) height = h
//        }

        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}