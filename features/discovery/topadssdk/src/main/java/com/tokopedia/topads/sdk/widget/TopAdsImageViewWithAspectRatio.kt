package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

const val BANNER_TYPE_HORIZONTAL = 1
const val BANNER_TYPE_VERTICAL = 2

class TopAdsImageViewWithAspectRatio @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {
    var imageWidth: Int? = null
    var imageHeight: Int? = null
    var bannerType: Int = -1
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        if (bannerType == BANNER_TYPE_HORIZONTAL && imageWidth != null && imageHeight != null) {
            val height = width * imageHeight!! / imageWidth!!
            setMeasuredDimension(width, height)
        } else if (bannerType == BANNER_TYPE_VERTICAL) {
            val height = width * 7 / 4
            setMeasuredDimension(width, height)
        }
    }
}
