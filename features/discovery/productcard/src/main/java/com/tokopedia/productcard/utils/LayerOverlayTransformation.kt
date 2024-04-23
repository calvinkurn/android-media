package com.tokopedia.productcard.utils

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import androidx.annotation.ColorInt
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class LayerOverlayTransformation(
    @ColorInt
    val color: Int,
    val radius: Float,
) : BitmapTransformation() {

    companion object {
        private const val ZERO_VALUE = 0f
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool[width, height, Bitmap.Config.ARGB_8888]
        bitmap.setHasAlpha(true)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        drawRoundRect(canvas, paint, width.toFloat(), height.toFloat())
        return bitmap
    }

    private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) {
        canvas.drawRoundRect(
            RectF(ZERO_VALUE, ZERO_VALUE, width, height),
            ZERO_VALUE,
            ZERO_VALUE,
            paint
        )
        val overlayPaint = Paint()
        overlayPaint.color = color
        canvas.drawRoundRect(
            RectF(ZERO_VALUE, ZERO_VALUE, width, height),
            radius,
            radius,
            overlayPaint
        )
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((this::class.java.name + color.toString()).toByteArray(CHARSET))
    }
}
