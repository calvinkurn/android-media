package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class TopAdsImageViewWithAspectRatio @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    var imageWidth: Int? = null
    var imageHeight: Int? = null
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        if (imageWidth != null && imageHeight != null) {
            val height = width * imageHeight!! / imageWidth!!
            setMeasuredDimension(width, height)
        }
    }
}
