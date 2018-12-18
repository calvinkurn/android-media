package com.tokopedia.kolcomponent.view.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.base.view.widget.TouchViewPager

/**
 * @author by milhamj on 17/12/18.
 */
class WrapContentViewPager : TouchViewPager {

    private val currentView: View? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var modifiedHeight = heightMeasureSpec
        if (currentView == null) {
            super.onMeasure(widthMeasureSpec, modifiedHeight)
            return
        }
        var height = 0
        currentView.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val h = currentView.measuredHeight
        if (h > height) height = h
        modifiedHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        super.onMeasure(widthMeasureSpec, modifiedHeight)
    }
}