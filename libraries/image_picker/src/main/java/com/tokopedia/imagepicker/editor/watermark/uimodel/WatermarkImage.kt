package com.tokopedia.imagepicker.editor.watermark.uimodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapUtils.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.MAX_IMAGE_SIZE

data class WatermarkImage(
    var image: Bitmap,
    @DrawableRes var imageDrawable: Int = 0,
    var alpha: Int = 50,
    var context: Context? = null,
    @FloatRange(from = 0.0, to = 1.0) var size: Double = 0.2,
    var position: WatermarkPosition = WatermarkPosition()
) {

    fun setPositionX(x: Double) {
        position.positionX = x
    }

    fun setPositionY(y: Double) {
        position.positionX = y
    }

    fun setRotation(rotation: Double) {
        position.rotation = rotation
    }

    fun watermarkFromImageView(imageView: ImageView) {
        imageView.invalidate()
        val drawable = imageView.drawable as BitmapDrawable
        image = resizeBitmap(drawable.bitmap, MAX_IMAGE_SIZE)
    }

    fun getBitmapFromDrawable(@DrawableRes imageDrawable: Int): Bitmap {
        return resizeBitmap(
            BitmapFactory.decodeResource(context?.resources, imageDrawable),
            MAX_IMAGE_SIZE
        )
    }

}