package com.tokopedia.media.loader.transform

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

class TopRightCrop : BitmapTransformation() {

    override fun equals(other: Any?): Boolean {
        return other is TopRightCrop
    }

    override fun hashCode(): Int {
        return ID.hashCode()
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun transform(pool: BitmapPool, inBitmap: Bitmap, width: Int, height: Int): Bitmap {
        if (inBitmap.width == width && inBitmap.height == height) {
            return inBitmap
        }

        // From ImageView/Bitmap.createScaledBitmap.
        val scale: Float
        val dx: Float
        val dy: Float
        val matrix = Matrix()
        if (inBitmap.width * height > width * inBitmap.height) {
            scale = height.toFloat() / inBitmap.height.toFloat()
            dx = (width - inBitmap.width * scale)
            dy = 0f
        } else {
            scale = width.toFloat() / inBitmap.width.toFloat()
            dx = 0f
            dy = (height - inBitmap.height * scale) * 0.5f
        }

        matrix.setScale(scale, scale)
        matrix.postTranslate((dx + 0.5f).toInt().toFloat(), ((dy + 0.5f).toInt().toFloat()))

        val result = pool[width, height, if (inBitmap.config != null) inBitmap.config else Bitmap.Config.ARGB_8888]

        result.setHasAlpha(inBitmap.hasAlpha())

        val canvas = Canvas(result)
        canvas.drawBitmap(inBitmap, matrix, DEFAULT_PAINT)
        canvas.setBitmap(null)

        return result
    }

    companion object {
        private const val ID = "com.tokopedia.productcard.utils.TopRightCrop"
        private val ID_BYTES = ID.toByteArray(Key.CHARSET)
        private val DEFAULT_PAINT = Paint(TransformationUtils.PAINT_FLAGS)
    }

}