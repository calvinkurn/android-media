package com.tokopedia.imagepicker.editor.watermark.utils

import android.content.Context
import android.graphics.*
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.util.TypedValue
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker.editor.main.Constant
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.changeColor
import timber.log.Timber
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object BitmapHelper {

    const val RESIZE_BITMAP_DEFAULT = 0.035f
    const val THRESHOLD_WHITE_SPACE = 20
    const val HEIGHT_MULTIPLIER_TEXT = 0.68f

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

        var boundWidth = bounds.width() + THRESHOLD_WHITE_SPACE // 20 is the threshold of white space
        var boundHeight = bounds.height()
        while (boundHeight < height * HEIGHT_MULTIPLIER_TEXT) {
            paint.textSize += 1
            paint.getTextBounds(
                this,
                0,
                this.length,
                bounds
            )
            boundHeight = bounds.height()
            boundWidth = bounds.width() + THRESHOLD_WHITE_SPACE
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

        val bitmapResult: Bitmap = if (boundWidth > 0 && boundHeight > 0)
            Bitmap.createBitmap(boundWidth, height, Bitmap.Config.ARGB_8888)
        else Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)


        // create the bitmap canvas
        val canvas = Canvas(bitmapResult)
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

    fun Bitmap.resizeBitmap(mainBitmap: Bitmap): Bitmap {
        val bitmapWidth = this.width
        val bitmapHeight = this.height
        val ratio = this.height.toFloat() / this.width

        val newHeight = if (mainBitmap.height < mainBitmap.width) {
            mainBitmap.height * RESIZE_BITMAP_DEFAULT
        } else {
            mainBitmap.width * RESIZE_BITMAP_DEFAULT
        }
        val newWidth = newHeight / ratio
        val scaleWidth = newWidth / bitmapWidth
        val scaleHeight = newHeight / bitmapHeight

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)

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

    fun Bitmap.combineBitmapWithPadding(other: Bitmap, mainBitmap: Bitmap): Bitmap {
        val scaledPaddingWidth = (0.2 * mainBitmap.width).toInt()
        val otherBitmap = other.addPadding(left = scaledPaddingWidth, right = scaledPaddingWidth)

        val scaledPaddingHeight = 0.2 * mainBitmap.height

        val width = this.width + otherBitmap.width
        val height = (maxOf(this.height, otherBitmap.height) + scaledPaddingHeight).toInt()

        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)

        val topPadding = ((this.height / 2f) - (other.height / 2f)).absoluteValue
        canvas.drawBitmap(this, 0f, 0f, null)
        canvas.drawBitmap(otherBitmap, this.width.toFloat(), 0f, null)
        return resultBitmap
    }

    fun Bitmap.combineBitmapTopDown(other: Bitmap): Bitmap {
        var thisPadding = 0f
        var otherPadding = 0f
        val padding = 20
        var height = this.height + other.height + padding
        val width = maxOf(this.width, other.width)
        val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(resultBitmap)
        val topPadding = ((this.width / 2f) - (other.width / 2f)).absoluteValue
        if (this.width < other.width) {
            thisPadding = topPadding
        } else {
            otherPadding = topPadding
        }
        canvas.drawBitmap(this, thisPadding, 0f, null)
        canvas.drawBitmap(other, otherPadding, (this.height + padding).toFloat(), null)
        return resultBitmap
    }

    //formula to determine brightness 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
    // if total of dark pixel > total of pixel * 0.45 count that as dark image
    fun Bitmap.isDark(): Boolean {
        try {
            val ratio = this.width / this.height
            val widthBitmapChecker = 50
            val heightBitmapChecker = widthBitmapChecker / ratio
            val bitmapChecker = Bitmap.createScaledBitmap(this, widthBitmapChecker, heightBitmapChecker, false)
            val darkThreshold = bitmapChecker.width * bitmapChecker.height * 0.45f
            var darkPixels = 0
            val pixels = IntArray(bitmapChecker.width * bitmapChecker.height)
            bitmapChecker.getPixels(pixels, 0, bitmapChecker.width, 0, 0, bitmapChecker.width, bitmapChecker.height)
            val luminanceThreshold = 150
            for (i in pixels.indices) {
                val color = pixels[i]
                val r = Color.red(color)
                val g = Color.green(color)
                val b = Color.blue(color)
                val luminance = 0.299 * r + 0.0f + 0.587 * g + 0.0f + 0.114 * b + 0.0f
                if (luminance < luminanceThreshold) {
                    darkPixels++
                }
            }
            bitmapChecker.recycle()
            return darkPixels >= darkThreshold
        } catch (t: Throwable) {
            return false
        }
    }

    fun Bitmap.changeColor(dstColor: Int): Bitmap {
        val width = this.width
        val height = this.height
        val srcHSV = FloatArray(3)
        val dstHSV = FloatArray(3)
        val dstBitmap = Bitmap.createBitmap(width, height, this.config)
        for (row in 0 until height) {
            for (col in 0 until width) {
                val pixel = this.getPixel(col, row)
                Color.colorToHSV(pixel, srcHSV)
                Color.colorToHSV(dstColor, dstHSV)
                val sourceAlpha = Color.alpha(pixel)

                dstBitmap.setPixel(col, row, Color.HSVToColor(sourceAlpha, dstHSV))
            }
        }
        this.recycle()
        return dstBitmap
    }

    //this value from 0..255
    fun Bitmap.setAlpha(alpha: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(
            width, // width in pixels
            height, // height in pixels
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            setAlpha(alpha)
        }
        canvas.drawBitmap(this, 0f, 0f, paint)
        return bitmap
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
     * if the text length > 13 the scaling value will be 0.12 from the mainBitmap
     * otherwise, the scaling will be 0.15 from the mainBitmap
     */
    fun Bitmap.downscaleToAllowedDimension(type: Int): Bitmap? {
        try {
            val scaleValue = if (type == Constant.TYPE_WATERMARK_TOPED) 1.0f else 2.0f

            val inWidth = this.width
            val inHeight = this.height
            val newHeight = inHeight * scaleValue

            val ratio = inHeight.toFloat() / inWidth
            val newWidth = newHeight / ratio

            val scaleWidth = newWidth / inWidth
            val scaleHeight = newHeight / inHeight

            val matrix = Matrix()
            matrix.postScale(scaleWidth, scaleHeight)

            return Bitmap.createBitmap(this, 0, 0, inWidth, inHeight, matrix, false)
        } catch (e: Throwable) {
            Timber.e(e)
            return this
        }
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