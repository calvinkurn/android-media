package com.tokopedia.home_component.customview.util

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import java.security.MessageDigest

class RoundedCornersTransformation constructor(
    private val radius: Int,
    private val cornerType: CornerType = CornerType.ALL
) : BitmapTransformation() {
    
    companion object {
        private const val ZERO_VALUE = 0f
    }

    enum class CornerType {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT
    }

    private val diameter: Int = radius * 2
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
        when (cornerType) {
            CornerType.TOP_LEFT -> drawTopLeftRoundRect(canvas, paint, width, height)
            CornerType.TOP_RIGHT -> drawTopRightRoundRect(canvas, paint, width, height)
            CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(canvas, paint, width, height)
            CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(canvas, paint, width, height)
            CornerType.TOP -> drawTopRoundRect(canvas, paint, width, height)
            CornerType.BOTTOM -> drawBottomRoundRect(canvas, paint, width, height)
            CornerType.LEFT -> drawLeftRoundRect(canvas, paint, width, height)
            CornerType.RIGHT -> drawRightRoundRect(canvas, paint, width, height)
            else -> canvas.drawRoundRect(
                RectF(ZERO_VALUE, ZERO_VALUE, width, height),
                radius.toFloat(),
                radius.toFloat(),
                paint
            )
        }
    }

    private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(
                ZERO_VALUE,
                ZERO_VALUE,
                diameter.toFloat(),
                diameter.toFloat()
            ), radius.toFloat(),
            radius.toFloat(), paint
        )
        canvas.drawRect(
            RectF(
                ZERO_VALUE,
                radius.toFloat(),
                radius.toFloat(),
                bottom
            ), paint
        )
        canvas.drawRect(RectF(radius.toFloat(), ZERO_VALUE, right, bottom), paint)
    }

    private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(right - diameter, ZERO_VALUE, right, diameter.toFloat()),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(ZERO_VALUE, ZERO_VALUE, right - radius, bottom), paint)
        canvas.drawRect(RectF(right - radius, radius.toFloat(), right, bottom), paint)
    }

    private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(ZERO_VALUE, bottom - diameter, diameter.toFloat(), bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(
            RectF(
                ZERO_VALUE,
                ZERO_VALUE,
                diameter.toFloat(),
                bottom - radius
            ), paint
        )
        canvas.drawRect(RectF(radius.toFloat(), ZERO_VALUE, right, bottom), paint)
    }

    private fun drawBottomRightRoundRect(
        canvas: Canvas,
        paint: Paint,
        right: Float,
        bottom: Float
    ) {
        canvas.drawRoundRect(
            RectF(right - diameter, bottom - diameter, right, bottom), radius.toFloat(),
            radius.toFloat(), paint
        )
        canvas.drawRect(RectF(ZERO_VALUE, ZERO_VALUE, right - radius, bottom), paint)
        canvas.drawRect(RectF(right - radius, ZERO_VALUE, right, bottom - radius), paint)
    }

    private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(ZERO_VALUE, ZERO_VALUE, right, ZERO_VALUE + diameter), radius.toFloat(), radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(ZERO_VALUE, ZERO_VALUE + radius, right, bottom), paint)
    }

    private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(ZERO_VALUE, bottom - diameter, right, bottom), radius.toFloat(), radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(ZERO_VALUE, ZERO_VALUE, right, bottom - radius), paint)
    }

    private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(ZERO_VALUE, ZERO_VALUE, ZERO_VALUE + diameter, bottom), radius.toFloat(), radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(ZERO_VALUE + radius, ZERO_VALUE, right, bottom), paint)
    }

    private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) {
        canvas.drawRoundRect(
            RectF(right - diameter, ZERO_VALUE, right, bottom),
            radius.toFloat(),
            radius.toFloat(),
            paint
        )
        canvas.drawRect(RectF(ZERO_VALUE, ZERO_VALUE, right - radius, bottom), paint)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((this::class.java.name + radius + diameter + cornerType).toByteArray(CHARSET))
    }
}
