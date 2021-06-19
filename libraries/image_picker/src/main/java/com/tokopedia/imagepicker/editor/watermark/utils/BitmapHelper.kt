package com.tokopedia.imagepicker.editor.watermark.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import androidx.core.content.res.ResourcesCompat
import kotlin.math.roundToInt
import com.tokopedia.imagepicker.editor.watermark.entity.Text as WatermarkText

object BitmapHelper {

    fun WatermarkText.textAsBitmap(context: Context): Bitmap {
        // to preventing the variable `shadows-issue`, define a new `shadow variable`
        val watermarkText = this

        // created TextPaint for painting the watermark text
        val paint = TextPaint().apply {
            // basic properties
            strokeWidth = 5f
            isAntiAlias = true
            color = watermarkText.textShadowColor
            style = watermarkText.textStyle
            textAlign = Paint.Align.LEFT

            // text alpha
            if (textAlpha in 0..255) {
                alpha = watermarkText.textAlpha
            }

            // text size in pixel based on device dimension
            val textInPixel = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                watermarkText.textSize.toFloat(),
                context.resources.displayMetrics
            )

            // text shadow properties
            if (watermarkText.textShadowBlurRadius != 0f
                || watermarkText.textShadowXOffset != 0f
                || watermarkText.textShadowYOffset != 0f) {
                setShadowLayer(
                    watermarkText.textShadowBlurRadius,
                    watermarkText.textShadowXOffset,
                    watermarkText.textShadowYOffset,
                    watermarkText.textShadowColor
                )
            }

            // font properties
            if (watermarkText.typeFaceId != 0) {
                typeface = ResourcesCompat.getFont(context, watermarkText.typeFaceId)
            }
        }

        // text bounds
        val baseline = (-paint.ascent() + 1f).toInt()
        val bounds = Rect()

        paint.getTextBounds(
            watermarkText.text,
            0,
            watermarkText.text.length,
            bounds
        )

        var boundWidth = bounds.width() + 20 // 20 is the threshold of white space
        val textMaxWidth = paint.measureText(watermarkText.text).toInt()

        if (boundWidth > textMaxWidth) {
            boundWidth = textMaxWidth
        }

        // create the static layout
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(text, 0, watermarkText.text.length, paint, textMaxWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(2.0f, 2.0f)
                .setIncludePad(false)
                .build()
        } else {
            StaticLayout(
                watermarkText.text,
                0,
                watermarkText.text.length,
                paint,
                textMaxWidth,
                Layout.Alignment.ALIGN_NORMAL,
                2.0f,
                2.0f,
                false
            )
        }

        // calculate the height of text bitmap
        val bitmapHeight = ((baseline + paint.descent() + 3) * staticLayout.lineCount).toInt()

        // create bitmap
        var bitmapResult = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        if (boundWidth > 0 && bitmapHeight > 0) {
            bitmapResult = Bitmap.createBitmap(boundWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
        }

        // create the bitmap canvas
        val canvas = Canvas(bitmapResult)
        canvas.drawColor(watermarkText.backgroundColor)

        staticLayout.draw(canvas)

        return bitmapResult
    }

    fun Bitmap.resizeBitmap(size: Float, background: Bitmap): Bitmap {
        val bitmapWidth = this.width
        val bitmapHeight = this.height
        val scale = (background.width * size) / bitmapWidth

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(
            this,
            0,
            0,
            bitmapWidth,
            bitmapHeight,
            matrix,
            true
        )
    }

    fun Bitmap.resizeBitmap(maxImageSize: Int): Bitmap {
        val ratio = (maxImageSize / width).toFloat()
            .coerceAtMost((maxImageSize / height).toFloat())

        val resultWidth = (ratio * width).roundToInt()
        val resultHeight = (ratio * height).roundToInt()

        return Bitmap.createScaledBitmap(
            this,
            resultWidth,
            resultHeight,
            true
        )
    }

}