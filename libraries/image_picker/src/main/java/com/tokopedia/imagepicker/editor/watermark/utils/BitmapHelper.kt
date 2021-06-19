package com.tokopedia.imagepicker.editor.watermark.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import kotlin.math.roundToInt

object BitmapHelper {

    fun String.textAsBitmap(context: Context, properties: TextUIModel): Bitmap {
        // created TextPaint for painting the watermark text
        val paint = TextPaint().apply {
            // basic properties
            strokeWidth = 5f
            isAntiAlias = true
            color = properties.textShadowColor
            style = properties.textStyle
            textAlign = Paint.Align.LEFT

            // text alpha
            if (properties.alpha in 0..255) {
                alpha = properties.alpha
            }

            // text size in pixel based on device dimension
            val textInPixel = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                properties.size.toFloat(),
                context.resources.displayMetrics
            )

            // text shadow properties
            if (properties.textShadowBlurRadius != 0f
                || properties.textShadowXOffset != 0f
                || properties.textShadowYOffset != 0f) {
                setShadowLayer(
                    properties.textShadowBlurRadius,
                    properties.textShadowXOffset,
                    properties.textShadowYOffset,
                    properties.textShadowColor
                )
            }

            // font properties
            if (properties.typeFaceId != 0) {
                typeface = ResourcesCompat.getFont(context, properties.typeFaceId)
            }
        }

        // text bounds
        val baseline = (-paint.ascent() + 1f).toInt()
        val bounds = Rect()

        paint.getTextBounds(
            this,
            0,
            this.length,
            bounds
        )

        var boundWidth = bounds.width() + 20 // 20 is the threshold of white space
        val textMaxWidth = paint.measureText(this).toInt()

        if (boundWidth > textMaxWidth) {
            boundWidth = textMaxWidth
        }

        // create the static layout
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(this, 0, this.length, paint, textMaxWidth)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(2.0f, 2.0f)
                .setIncludePad(false)
                .build()
        } else {
            StaticLayout(
                this,
                0,
                this.length,
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
        canvas.drawColor(properties.backgroundColor)

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

    fun getBitmapFromDrawable(
        context: Context?,
        @DrawableRes imageDrawable: Int
    ): Bitmap {
        return BitmapFactory
            .decodeResource(context?.resources, imageDrawable)
            .resizeBitmap(MAX_IMAGE_SIZE)
    }

}