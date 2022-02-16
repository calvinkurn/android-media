package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.editor.main.Constant
import com.tokopedia.imagepicker.editor.watermark.entity.BaseWatermark
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextAndImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.changeColor
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.combineBitmapTopDown
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.combineBitmapWithPadding
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.downscaleToAllowedDimension
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.textAsBitmap
import java.lang.IllegalArgumentException
import android.graphics.Bitmap.createBitmap as createBitmap

data class Watermark(
    var context: Context,
    var backgroundImg: Bitmap? = null,
    var watermarkImg: ImageUIModel? = null,
    var watermarkText: TextUIModel? = null,
    var watermarkTextAndImage: TextAndImageUIModel? = null,
    var outputImage: Bitmap? = null,
    var watermarkBitmap: Bitmap? = null,
    var scaledWatermarkBitmap: Bitmap? = null,
    var isTitleMode: Boolean,
    var isCombine: Boolean,
    var onlyWatermark: Boolean,
    var type: Int,
    var isDark: Boolean
) {

    init {
        watermarkTextAndImage?.textShadowColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N100)

        if (!isCombine) {
            createWatermarkImage(watermarkImg)
            createWatermarkText(watermarkText)
        } else {
            if (onlyWatermark) {
                createScalableWatermarkTextAndImage(watermarkTextAndImage)
            } else {
                createWatermarkTextAndImage(watermarkTextAndImage)
            }
        }
    }

    /**
     * build scalable watermark bitmap with text and image,
     * the output stored to [watermarkBitmap]
     * @param: [TextAndImageUIModel]
     */
    private fun createScalableWatermarkTextAndImage(watermark: TextAndImageUIModel?) {
        if (watermark == null) return

        val resizedBitmap = watermark.image!!.resizeBitmap(backgroundImg!!)

        val logoBitmap = resizedBitmap

        val textBitmap = watermark.text.textAsBitmap(context, watermark, logoBitmap.height)

        val combinedBitmap = when (this.type) {
            Constant.TYPE_WATERMARK_TOPED -> {
                logoBitmap.combineBitmapWithPadding(textBitmap, backgroundImg!!)
            }
            Constant.TYPE_WATERMARK_CENTER_TOPED -> {
                logoBitmap.combineBitmapTopDown(textBitmap)
            }
            else -> {
                logoBitmap.combineBitmapWithPadding(textBitmap, backgroundImg!!)
            }
        }

        createConfigWatermark(
            bitmap = combinedBitmap,
            config = watermark
        )
        logoBitmap.recycle()
        textBitmap.recycle()
    }

    /**
     * build watermark bitmap with text and image,
     * the output stored to [watermarkBitmap]
     * @param: [TextAndImageUIModel]
     */
    private fun createWatermarkTextAndImage(watermark: TextAndImageUIModel?) {
        if (watermark == null) return

        val logoBitmap = watermark.image!!.resizeBitmap(watermark.imageSize.toFloat(), backgroundImg!!.width)
        val textBitmap = watermark.text.textAsBitmap(context, watermark, logoBitmap.height)

        createWatermark(
            bitmap = logoBitmap.combineBitmapWithPadding(textBitmap, backgroundImg!!),
            config = watermark
        )
    }

    /**
     * build watermark bitmap with image,
     * the output stored to [watermarkBitmap]
     * @param: [ImageUIModel]
     */
    private fun createWatermarkImage(watermarkImg: ImageUIModel?) {
        if (watermarkImg == null) return

        val bitmap = watermarkImg.image!!.resizeBitmap(
            watermarkImg.imageSize.toFloat(),
            backgroundImg!!.width
        )

        createWatermark(
            bitmap = bitmap,
            config = watermarkImg
        )
    }

    /**
     * build watermark bitmap with text,
     * the output stored to [watermarkBitmap]
     * @param: [TextUIModel]
     */
    private fun createWatermarkText(watermarkText: TextUIModel?) {
        if (watermarkText == null) return

        val textBitmap = watermarkText.text.textAsBitmap(context, watermarkText)

        createWatermark(
            bitmap = textBitmap,
            config = watermarkText
        )
    }

    private fun createConfigWatermark(
        bitmap: Bitmap,
        config: BaseWatermark?
    ) {
        if (config == null) return

        var textLength = 0

        val paint = Paint().apply {
            val bitmapAlpha = when (config) {
                is TextUIModel -> {
                    // if config is TextUIModel, then
                    // get the length of text to calculate
                    // the scaling of watermark bitmap
                    textLength = config.text.length

                    config.textAlpha
                }
                is ImageUIModel -> {
                    config.imageAlpha
                }
                else -> 0
            }

            alpha = bitmapAlpha
        }
        watermarkBitmap = bitmap

        if (!isDark) {
            watermarkBitmap = watermarkBitmap!!
                .changeColor(MethodChecker.getColor(context, R.color.green_neutral_30))
        }

        scaledWatermarkBitmap().apply {
            outputImage = this
        }
    }

    private fun createWatermark(
        bitmap: Bitmap,
        config: BaseWatermark?
    ) {
        if (config == null) return

        val paint = Paint().apply {
            val bitmapAlpha = when (config) {
                is TextUIModel -> config.textAlpha
                is ImageUIModel -> config.imageAlpha
                else -> 0
            }

            alpha = bitmapAlpha
        }

        backgroundImg?.let {
            val newBitmap = createBitmap(it.width, it.height, it.config)
            val canvas = Canvas(newBitmap)

//            canvas.drawBitmap(canvasBitmap!!, 0f, 0f, null)

            if (isTitleMode) {
                paint.shader = BitmapShader(
                    watermarkBitmap!!,
                    Shader.TileMode.REPEAT,
                    Shader.TileMode.REPEAT
                )

                canvas.drawRect(canvas.clipBounds, paint)
            } else {
                canvas.drawBitmap(
                    watermarkBitmap!!,
                    (config.position.positionX * it.width).toFloat(),
                    (config.position.positionY * it.height).toFloat(),
                    paint
                )
            }

            outputImage = newBitmap
        }
    }

    private fun scaledWatermarkBitmap(): Bitmap {
        scaledWatermarkBitmap =
                watermarkBitmap!!.downscaleToAllowedDimension(this.type)

        // merge the main bitmap with scaled watermark bitmap
        outputImage = mapWatermarkType(this.type)

        watermarkBitmap?.recycle()
        scaledWatermarkBitmap?.recycle()

        return outputImage!!
    }

    private fun tileWatermarkBitmap(mainBitmap: Bitmap): Bitmap {
        Canvas(mainBitmap).apply {
            // first, draw the main bitmap into canvas
            drawBitmap(backgroundImg!!, 0f, 0f, null)
            rotate(-30f)
            this.clipBounds.top = 600

            // afterwards, draw tiles mode of watermark
            drawRect(this.clipBounds, Paint().apply {
                shader = BitmapShader(
                    scaledWatermarkBitmap!!,
                    Shader.TileMode.REPEAT,
                    Shader.TileMode.REPEAT
                )
            })
        }
        return mainBitmap
    }

    /**
     * ignore watermark right padding by subtracting watermark width with amount of right padding
     * right padding equals 200
     */
    private fun centerWatermarkBitmap(mainBitmap: Bitmap): Bitmap {
        Canvas(mainBitmap).apply {
            // first, draw the main bitmap into canvas
            drawBitmap(backgroundImg!!, 0f, 0f, null)

            //afterwards, draw watermark on the center
            drawBitmap(scaledWatermarkBitmap!!,
                (backgroundImg!!.width / 2f - (scaledWatermarkBitmap!!.width / 2f)),
                (backgroundImg!!.height / 2f) - (scaledWatermarkBitmap!!.height / 2f), null)
        }

        return mainBitmap
    }

    private fun mapWatermarkType(type: Int): Bitmap {
        val resultBitmap = backgroundImg!!.copy(Bitmap.Config.RGB_565, true)
        return when (type) {
            Constant.TYPE_WATERMARK_TOPED -> {
                tileWatermarkBitmap(resultBitmap)
            }
            Constant.TYPE_WATERMARK_CENTER_TOPED -> {
                centerWatermarkBitmap(resultBitmap)
            }
            else -> throw IllegalArgumentException()
        }
    }
}