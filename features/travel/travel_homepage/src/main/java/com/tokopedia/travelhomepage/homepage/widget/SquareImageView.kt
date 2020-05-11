package com.tokopedia.travelhomepage.homepage.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @author by jessica on 2020-03-16
 */

class SquareImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
        AppCompatImageView (context, attrs, defStyleAttr) {

    override fun onMeasure(width: Int, height: Int) {
        super.onMeasure(width, height)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

}