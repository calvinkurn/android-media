package com.tokopedia.home_page_banner.presenter.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView


class BottomCropImage : ImageView{
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        setup()
    }

    private fun setup() {
        scaleType = ScaleType.MATRIX
    }
    override fun setFrame(frameLeft: Int, frameTop: Int, frameRight: Int, frameBottom: Int): Boolean {
        val drawable: Drawable? = drawable
        if (drawable != null) {
            val frameWidth: Float = frameRight.toFloat() - frameLeft.toFloat()
            val frameHeight: Float = frameBottom.toFloat() - frameTop.toFloat()
            val originalImageWidth = getDrawable().intrinsicWidth.toFloat()
            val originalImageHeight = getDrawable().intrinsicHeight.toFloat()
            var usedScaleFactor = 1f
            if (frameWidth > originalImageWidth || frameHeight > originalImageHeight) { // If frame is bigger than image
            // => Crop it, keep aspect ratio and position it at the bottom
            // and
            // center horizontally
                val fitHorizontallyScaleFactor = frameWidth / originalImageWidth
                val fitVerticallyScaleFactor = frameHeight / originalImageHeight
                usedScaleFactor = Math.max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor)
            }
            val newImageWidth = originalImageWidth * usedScaleFactor
            val newImageHeight = originalImageHeight * usedScaleFactor
            val matrix = imageMatrix
            matrix.setScale(usedScaleFactor, usedScaleFactor, 0f, 0f)
            matrix.postTranslate((frameWidth - newImageWidth) / 2, frameHeight - newImageHeight)
            imageMatrix = matrix
        }
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom)
    }
}