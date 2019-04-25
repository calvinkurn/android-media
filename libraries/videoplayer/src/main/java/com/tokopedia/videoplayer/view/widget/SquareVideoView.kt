package com.tokopedia.videoplayer.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.VideoView

/**
 * Created by isfaaghyth on 13/04/19.
 * github: @isfaaghyth
 */
class SquareVideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null,
        defStyleAttr: Int = 0): VideoView(context, attrs, defStyleAttr) {

    /**
     * make a perfect square in any size of devices
     * onMeasure()
     * @param: widthMeasureSpec
     * @param: heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        val size = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(size, size)
    }

}