package com.tokopedia.imagepicker.editor.watermark.builder

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.data.ImageDefault
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeScaledBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.MAX_IMAGE_SIZE

class Image : ImageUIModel, ImageDefault() {

    fun imageFromImageView(imageView: ImageView) = apply {
        watermarkFromImageView(imageView)
    }

    private fun watermarkFromImageView(imageView: ImageView) {
        imageView.invalidate()
        val drawable = imageView.drawable as BitmapDrawable
        image = drawable.bitmap.resizeScaledBitmap(MAX_IMAGE_SIZE)
    }

}