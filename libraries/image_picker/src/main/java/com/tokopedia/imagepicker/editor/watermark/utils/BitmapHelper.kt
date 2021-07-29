package com.tokopedia.imagepicker.editor.watermark.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.downscaleToAllowedDimension
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object BitmapHelper {

    fun String.textAsBitmap(context: Context, properties: TextUIModel, height: Int = 0): Bitmap {
        // created TextPaint for painting the watermark text
        val paint = TextPaint().apply {
            // text size in pixel based on device dimension
            val textInPixel = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    properties.textSize.toFloat(),
                    context.resources.displayMetrics
            )

            // basic properties
            textSize = properties.textSize.toFloat()
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
        var boundHeight = bounds.height()
        while (boundHeight < height - 20) {
            paint.textSize += 1
            paint.getTextBounds(
                    this,
                    0,
                    this.length,
                    bounds
            )
            boundHeight = bounds.height()
            boundWidth = bounds.width() + 20
            println("cek height ${boundHeight}")
        }
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
//        val bitmapHeight = ((baseline + paint.descent() + 3) * staticLayout.lineCount).toInt()
        // create bitmap
        var bitmapResult = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        if (boundWidth > 0 && boundHeight > 0) {
            bitmapResult = Bitmap.createBitmap(boundWidth, height, Bitmap.Config.ARGB_8888)
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

    fun Bitmap.resizebitmapAndKeepRatio(newHeight: Int): Bitmap {
        val ratio = this.height.toFloat() / this.width
        val newWidth = newHeight / ratio

        val scaleWidth = newWidth / this.width
        val scaleHeight = newHeight.toFloat() / this.height

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, false)
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
        var thisPadding = 0f;
        var otherPadding = 0f
        val thresholdSpace = 170 // space between first bitmap to other bitmap
        val otherBitmap = other.addPadding(left = thresholdSpace, right = thresholdSpace)


        val width = this.width + otherBitmap.width
        val height = maxOf(this.height, otherBitmap.height) + 75

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        println("cek this ${this.height} other ${other.height}")
        val topPadding = ((this.height / 2f) - (other.height / 2f)).absoluteValue

        if (this.height < other.height)
            thisPadding = topPadding
        else
            otherPadding = topPadding

        canvas.drawBitmap(this, 0f, thisPadding, null)
        canvas.drawBitmap(otherBitmap, this.width.toFloat(), otherPadding, null)
        return resultBitmap
    }

    fun Bitmap.isDark(): Boolean {
        var isDark = false
        val darkThreshold = this.width * this.height * 0.45f
        var darkPixels = 0
        val pixels = IntArray(this.width * this.height)
        this.getPixels(pixels, 0, this.width, 0, 0, this.width, this.height)
        for (i in pixels.indices) {
            val color = pixels[i]
            val r = Color.red(color)
            val g = Color.green(color)
            val b = Color.blue(color)
            val luminance = 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
            if (luminance < 150) {
                darkPixels++
            }
        }
        if (darkPixels >= darkThreshold) {
            isDark = true
        }
        return isDark
    }

    fun Bitmap.changeColor(dstColor: Int): Bitmap {
        val width = this.width
        val height = this.height
        val srcHSV = FloatArray(3)
        val dstHSV = FloatArray(3)
        val dstBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (row in 0 until height) {
            for (col in 0 until width) {
                val pixel = this.getPixel(col, row)
                val alpha = Color.alpha(pixel)
                Color.colorToHSV(pixel, srcHSV)
                Color.colorToHSV(dstColor, dstHSV)

                // If it area to be painted set only value of original image
                dstHSV[2] = srcHSV[2] // value
                dstBitmap.setPixel(col, row, Color.HSVToColor(alpha, dstHSV))
            }
        }
        return dstBitmap
    }

    fun Bitmap.addPadding(
            left: Int = 0,
            top: Int = 0,
            right: Int = 0,
            bottom: Int = 0
    ): Bitmap {
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
                    Rect(left, top, bitmap.width - right, bitmap.height - bottom),
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
    fun Bitmap.downscaleToAllowedDimension(mainBitmap: Bitmap, textLength: Int): Bitmap? {

        val inWidth = this.width
        val inHeight = this.height

        val ratio = inHeight.toFloat() / inWidth

        val newHeight = mainBitmap.height * 0.1f
        val newWidth = newHeight / ratio

        val scaleWidth = newWidth / inWidth
        val scaleHeight = newHeight / inHeight

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

        return Bitmap.createBitmap(this, 0, 0, inWidth, inHeight, matrix, false)
    }

    private fun Bitmap.scaleByDividedOfThreesHold(textLength: Int): Float {
        if (textLength >= 13) return this.width / 2f
        if (this.width == this.height) return this.width / 2f

        return if (this.width > this.height) {
            this.width / 3f
        } else {
            this.height / 3.5f
        }
    }

}