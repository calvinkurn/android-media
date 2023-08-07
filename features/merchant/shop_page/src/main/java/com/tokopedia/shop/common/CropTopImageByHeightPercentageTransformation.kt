package com.tokopedia.shop.common

import android.graphics.Bitmap
import android.graphics.Canvas
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CropTopImageByHeightPercentageTransformation(
    private val percentageOfHeight: Double
) : BitmapTransformation() {
    override fun transform(
        pool: BitmapPool,
        bitmap: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return cutTop(bitmap, bitmap.height * percentageOfHeight)
    }

    private fun cutTop(bitmap: Bitmap, cutHeight: Double): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        // Calculate the new height after cutting the top portion
        val newHeight = height - cutHeight

        // Create a new bitmap with the updated dimensions
        val croppedBitmap = Bitmap.createBitmap(width, newHeight.toInt(), Bitmap.Config.ARGB_8888)

        // Create a canvas and draw the original bitmap on it, skipping the top portion
        val canvas = Canvas(croppedBitmap)
        canvas.drawBitmap(bitmap, 0f, (-cutHeight).toFloat(), null)

        return croppedBitmap
    }

    override fun equals(other: Any?): Boolean {
        return other is CropTopImageByHeightPercentageTransformation
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    companion object {
        private const val ID = "CropTopImageByHeightPercentageTransformation"
        private val ID_BYTES = ID.toByteArray(Charsets.UTF_8)
    }
}
