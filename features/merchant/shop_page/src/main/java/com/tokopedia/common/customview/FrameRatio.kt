package com.tokopedia.common.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tokopedia.shop.R as shopR

class FrameRatio : FrameLayout {

    var widthRatio: Int = 1
    var heightRatio: Int = 1

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initWittAttrs(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initWittAttrs(context, attrs)
    }

    private fun initWittAttrs(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, shopR.styleable.FrameRatio)
        widthRatio = attributeArray.getInt(
            shopR.styleable.FrameRatio_frame_ratio_width,
            1
        )
        heightRatio = attributeArray.getInt(
            shopR.styleable.FrameRatio_frame_ratio_height,
            1
        )
        attributeArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val originalWidth = MeasureSpec.getSize(widthMeasureSpec)

        val heightWidthRatio = heightRatio.toFloat() / widthRatio

        val calculatedHeight = originalWidth * heightWidthRatio

        val finalWidth: Int = originalWidth
        val finalHeight: Int = calculatedHeight.toInt()

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY)
        )
    }
}
