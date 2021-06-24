package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.entity.BaseWatermark
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextAndImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.adjustRotation
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.combine
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
    var isTitleMode: Boolean,
    var isCombine: Boolean
) {

    init {
        canvasBitmap = backgroundImg
        outputImage = backgroundImg

        if (!isCombine) {
            createWatermarkImage(watermarkImg)
            createWatermarkText(watermarkText)
        } else {
            createWatermarkTextAndImage(watermarkTextAndImage)
        }
    }

    private fun createWatermarkTextAndImage(watermark: TextAndImageUIModel?) {
        if (watermark == null) return

        val logoBitmap = watermark.image!!.resizeBitmap(watermark.imageSize.toFloat(), backgroundImg!!)
        val textBitmap = watermark.text.textAsBitmap(context, watermark)

        createWatermark(
            bitmap = logoBitmap.combine(textBitmap),
            config = watermark
        )
    }

    private fun createWatermarkImage(watermarkImg: ImageUIModel?) {
        if (watermarkImg == null) return

        val bitmap = watermarkImg.image!!.resizeBitmap(watermarkImg.imageSize.toFloat(), backgroundImg!!)

        createWatermark(
            bitmap = bitmap,
            config = watermarkImg
        )
    }

    private fun createWatermarkText(watermarkText: TextUIModel?) {
        if (watermarkText == null) return

        val textBitmap = watermarkText.text.textAsBitmap(context, watermarkText)

        createWatermark(
            bitmap = textBitmap,
            config = watermarkText
        )
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

            val hasWatermarkBitmap = bitmap.adjustRotation(config.position.rotation)

            if (isTitleMode) {
                paint.shader = BitmapShader(
                    hasWatermarkBitmap,
                    Shader.TileMode.REPEAT,
                    Shader.TileMode.REPEAT
                )

                val bitmapShaderRect = canvas.clipBounds
                canvas.drawRect(bitmapShaderRect, paint)
            } else {
                canvas.drawBitmap(
                    hasWatermarkBitmap,
                    (config.position.positionX * it.width).toFloat(),
                    (config.position.positionY * it.height).toFloat(),
                    paint
                )
            }

            canvasBitmap = newBitmap
            outputImage = newBitmap
        }
    }

    fun setToImageView(target: ImageView) {
        target.setImageBitmap(outputImage)
    }

}