package com.tokopedia.videorecorder.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by isfaaghyth on 01/03/19.
 * github: @isfaaghyth
 */
class DynamicSquareLayout(context: Context, attributeSet: AttributeSet): RelativeLayout(context, attributeSet) {

    /**
     * make a perfect square in any size of devices
     * onMeasure()
     * @param: widthMeasureSpec
     * @param: heightMeasureSpec
     */
    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        val size = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(size, size)
    }

}