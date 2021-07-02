package com.tokopedia.imagepicker.editor.watermark.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import kotlin.math.roundToInt

object BitmapHelper {

    fun String.textAsBitmap(context: Context, properties: TextUIModel): Bitmap {
        // created TextPaint for painting the watermark text
        val paint = TextPaint().apply {
            // text size in pixel based on device dimension
            val textInPixel = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                properties.textSize.toFloat(),
                context.resources.displayMetrics
            )

            // basic properties
            textSize = textInPixel
            strokeWidth = 5f
            isAntiAlias = true
            color = properties.textShadowColor
            style = properties.textStyle
            textAlign = Paint.Align.LEFT

            // text alpha
            if (properties.textAlpha in 0..255) {
                alpha = properties.textAlpha
            }

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
            if (properties.fontName.isNotEmpty()) {
                typeface = com.tokopedia.unifyprinciples.getTypeface(context, properties.fontName)
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

    fun Bitmap.resizeBitmap(size: Float, backgroundWidth: Int): Bitmap {
        val bitmapWidth = this.width
        val bitmapHeight = this.height
        val scale = (backgroundWidth * size) / bitmapWidth

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

    fun Bitmap.resizeScaledBitmap(maxImageSize: Int): Bitmap {
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

    fun Bitmap.adjustRotation(orientationAngle: Double): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(
            orientationAngle.toFloat(),
            (this.width / 2).toFloat(),
            (this.height / 2).toFloat()
        )

        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    fun Bitmap.combineBitmapWithPadding(other: Bitmap): Bitmap {
        val thresholdSpace = 170 // space between first bitmap to other bitmap
        val otherBitmap = other.addPadding(thresholdSpace, 0, thresholdSpace, 0)

        val width = this.width + otherBitmap.width
        val height = maxOf(this.height, otherBitmap.height)

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(this, 0f, 0f, null)
        canvas.drawBitmap(otherBitmap, this.width.toFloat(), 0f, null)

        return resultBitmap
    }

    fun Bitmap.addPadding(
        left:Int = 0,
        top:Int = 0,
        right:Int = 0,
        bottom:Int = 0
    ):Bitmap {
        val bitmap = Bitmap.createBitmap(
            width + left + right, // width in pixels
            height + top + bottom, // height in pixels
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        // clear color from bitmap drawing area
        // this is very important for transparent bitmap borders
        // this will keep bitmap transparency
        Paint().apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            canvas.drawRect(
                Rect(left,top,bitmap.width - right,bitmap.height - bottom),
                this
            )
        }

        // finally, draw bitmap on canvas
        Paint().apply {
            canvas.drawBitmap(
                this@addPadding, // bitmap
                0f + left, // left
                0f + top, // top
                this // paint
            )
        }

        return bitmap
    }

    /**
     * resize the watermark bitmap by scaling with specific condition
     * if text length (the user info name) have length more than 25,
     * the size of watermark bitmap it will be resizing into (width / 2)
     * otherwise, the scaling will be perform (width / 3) or (height / 4) based on orientation.
     *
     * the scaling will be calculate it on [scaleByDividedOfThreesHold]
     */
    fun Bitmap.downscaleToScaledAllowedDimension(mainBitmap: Bitmap, textLength: Int): Bitmap? {
        val ratioThreshold = mainBitmap.scaleByDividedOfThreesHold(textLength)

        val inWidth = this.width
        val inHeight = this.height

        val outWidth: Int
        val outHeight: Int

        // scaling based on bitmap orientation
        if (inWidth > inHeight) {
            outWidth = ratioThreshold
            outHeight = inHeight * ratioThreshold / inWidth
        } else {
            outHeight = ratioThreshold
            outWidth = inWidth * ratioThreshold / inHeight
        }

        return Bitmap.createScaledBitmap(
            this,
            outWidth,
            outHeight,
            false
        )
    }

    private fun Bitmap.scaleByDividedOfThreesHold(textLength: Int): Int {
        if (textLength >= 13) return this.width / 2

        return if (this.width > this.height || this.width == this.height) {
            this.width / 3
        } else {
            this.height / 4
        }
    }

}