package com.tokopedia.imagepicker.editor.watermark.builder

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.data.ImageDefault
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.MAX_IMAGE_SIZE

class Image : ImageUIModel, ImageDefault() {

    fun setImageBitmap(value: Bitmap) = apply {
        this.image = value
    }

    fun positionX(x: Double) = apply {
        position.positionX = x
    }

    fun positionY(y: Double) = apply {
        position.positionX = y
    }

    fun rotation(rotation: Double) = apply {
        position.rotation = rotation
    }

    fun imageAlpha(value: Int) = apply {
        this.alpha = value
    }

    fun imageSize(value: Double) = apply {
        this.imageSize = value
    }

    fun imageFromImageView(imageView: ImageView) = apply {
        watermarkFromImageView(imageView)
    }

    private fun watermarkFromImageView(imageView: ImageView) {
        imageView.invalidate()
        val drawable = imageView.drawable as BitmapDrawable
        image = drawable.bitmap.resizeBitmap(MAX_IMAGE_SIZE)
    }

}