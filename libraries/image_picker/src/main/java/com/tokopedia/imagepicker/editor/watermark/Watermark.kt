package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.entity.BaseWatermark
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextAndImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.addPadding
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.adjustRotation
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.combineBitmapWithPadding
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.downscaleToScaledAllowedDimension
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.textAsBitmap
import android.graphics.Bitmap.createBitmap as createBitmap

data class Watermark (
    var context: Context,
    var backgroundImg: Bitmap? = null,
    var watermarkImg: ImageUIModel? = null,
    var watermarkText: TextUIModel? = null,
    var watermarkTextAndImage: TextAndImageUIModel? = null,
    var outputImage: Bitmap? = null,
    var canvasBitmap: Bitmap? = null,
    var watermarkBitmap: Bitmap? = null,
    var isTitleMode: Boolean,
    var isCombine: Boolean,
    var onlyWatermark: Boolean
) {

    init {
        canvasBitmap = backgroundImg
        outputImage = backgroundImg

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

        // threesHold of empty bitmap as container of watermark
        val squareBitmapSize = 2000

        val logoBitmap = watermark.image!!.resizeBitmap(watermark.imageSize.toFloat(), squareBitmapSize)
        val textBitmap = watermark.text.textAsBitmap(context, watermark)

        createScaledWatermark(
            bitmap = logoBitmap.combineBitmapWithPadding(textBitmap),
            config = watermark
        )
    }

    /**
     * build watermark bitmap with text and image,
     * the output stored to [watermarkBitmap]
     * @param: [TextAndImageUIModel]
     */
    private fun createWatermarkTextAndImage(watermark: TextAndImageUIModel?) {
        if (watermark == null) return

        val logoBitmap = watermark.image!!.resizeBitmap(watermark.imageSize.toFloat(), backgroundImg!!.width)
        val textBitmap = watermark.text.textAsBitmap(context, watermark)

        createWatermark(
            bitmap = logoBitmap.combineBitmapWithPadding(textBitmap),
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

    private fun createScaledWatermark(
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

        bitmap.adjustRotation(config.position.rotation).also {
            watermarkBitmap = it
        }.let {
            val watermarkWithAlphaBitmap = createBitmap(it.width, it.height, it.config)
            val canvas = Canvas(watermarkWithAlphaBitmap)
            canvas.drawBitmap(it, 0f, 0f, paint)
        }

        val resultBitmap = scaledWatermarkBitmap(textLength)

        canvasBitmap = resultBitmap
        outputImage = resultBitmap
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

            canvas.drawBitmap(canvasBitmap!!, 0f, 0f, null)

            watermarkBitmap = bitmap.adjustRotation(config.position.rotation)

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

            canvasBitmap = newBitmap
            outputImage = newBitmap
        }
    }

    private fun scaledWatermarkBitmap(textLength: Int): Bitmap {
        // get the width size of main bitmap for resizing the watermark container
        val widthMainBitmap = backgroundImg!!.width
        val heightMainBitmap = backgroundImg!!.height

        // scaled resize the watermark container with divided by three
        val scaledWatermarkBitmap =
            watermarkBitmap!!.downscaleToScaledAllowedDimension(
                mainBitmap = backgroundImg!!,
                textLength = textLength
            )?.addPadding(left = 10)

        // merge the main bitmap with scaled watermark bitmap
        val resultBitmap = createBitmap(
            widthMainBitmap,
            heightMainBitmap,
            backgroundImg!!.config
        )

        val canvas = Canvas(resultBitmap)

        canvas.drawBitmap(backgroundImg!!, 0f, 0f, null)
        canvas.drawRect(canvas.clipBounds, Paint().apply {
            shader = BitmapShader(
                scaledWatermarkBitmap!!,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
            )
        })

        return resultBitmap
    }

}