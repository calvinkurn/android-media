package com.tokopedia.merchantvoucher.common.widget

import android.content.Context
import android.graphics.Matrix
import android.util.AttributeSet
import android.widget.ImageView


class MerchantVoucherImageType(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    override fun setFrame(frameLeft: Int, frameTop: Int, frameRight: Int, frameBottom: Int): Boolean {
        drawable?.run {
            val frameWidth = frameRight - frameLeft
            val frameHeight = frameBottom - frameTop

            val originalImageWidth = intrinsicWidth.toFloat()
            val originalImageHeight = intrinsicHeight.toFloat()

            val fitHorizontallyScaleFactor = frameWidth / originalImageWidth
            val fitVerticallyScaleFactor = frameHeight / originalImageHeight

            val usedScaleFactor = fitHorizontallyScaleFactor.coerceAtLeast(fitVerticallyScaleFactor)

            val newImageHeight = originalImageHeight * usedScaleFactor

            imageMatrix = Matrix().apply {
                reset()
                postTranslate(0f, (frameHeight - newImageHeight) / 2f)
                preScale(usedScaleFactor, usedScaleFactor, 0f, 0f)
            }
        }

        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom)
    }
}