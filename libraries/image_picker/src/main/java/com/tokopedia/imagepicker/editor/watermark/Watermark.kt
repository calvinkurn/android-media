package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.entity.BaseWatermark
import com.tokopedia.imagepicker.editor.watermark.entity.ImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextAndImageUIModel
import com.tokopedia.imagepicker.editor.watermark.entity.TextUIModel
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.textAsBitmap

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

        val logoBitmap = watermark.image!!.resizeBitmap(
            size = watermark.imageSize.toFloat(),
            background = backgroundImg!!
        )

        val textBitmap = watermark.text.textAsBitmap(context, watermark)

        createWatermark(
            bitmap = logoBitmap.combine(textBitmap),
            config = watermark
        )
    }

    private fun createWatermarkImage(watermarkImg: ImageUIModel?) {
        if (watermarkImg == null) return

        createWatermark(
            bitmap = watermarkImg.image!!.resizeBitmap(
                size = watermarkImg.imageSize.toFloat(),
                background = backgroundImg!!
            ),
            config = watermarkImg
        )
    }

    private fun createWatermarkText(watermarkText: TextUIModel?) {
        if (watermarkText == null) return

        createWatermark(
            bitmap = watermarkText.text.textAsBitmap(context, watermarkText),
            config = watermarkText
        )
    }

    private fun createWatermark(
        bitmap: Bitmap,
        config: BaseWatermark?
    ) {
        if (config == null) return

        val paint = Paint().apply {
            alpha = config.alpha
        }

        backgroundImg?.let {
            val newBitmap = Bitmap.createBitmap(it.width, it.height, it.config)
            val watermarkCanvas = Canvas(newBitmap)

            watermarkCanvas.drawBitmap(canvasBitmap!!, 0f, 0f, null)

            val watermarkBitmap = adjustRotation(
                bitmap,
                config.position.rotation.toInt()
            )

            if (isTitleMode) {
                paint.shader = BitmapShader(
                    watermarkBitmap,
                    Shader.TileMode.REPEAT,
                    Shader.TileMode.REPEAT
                )

                val bitmapShaderRect = watermarkCanvas.clipBounds
                watermarkCanvas.drawRect(bitmapShaderRect, paint)
            } else {
                watermarkCanvas.drawBitmap(
                    watermarkBitmap,
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

    private fun adjustRotation(bitmap: Bitmap, orientationAngle: Int): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(
            orientationAngle.toFloat(),
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat()
        )

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun Bitmap.addSpace(spaceCount: Int): Bitmap {
        val combinedCanvas = Canvas(this)
        combinedCanvas.drawBitmap(
            this,
            this.width.toFloat() * spaceCount,
            0f,
            null
        )
        return this
    }

    private fun Bitmap.combine(secondBitmap: Bitmap): Bitmap {
        val spaceThreshold = 2
        val width: Int
        val height: Int

        if (this.width > secondBitmap.width) {
            width = (this.width + secondBitmap.width) * spaceThreshold
            height = this.height
        } else {
            width = (secondBitmap.width + secondBitmap.width) * spaceThreshold
            height = this.height
        }

        val combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val combinedCanvas = Canvas(combinedBitmap)

        combinedCanvas.drawBitmap(this, 0f, 0f, null)
        combinedCanvas.drawBitmap(secondBitmap, this.width.toFloat() * spaceThreshold, 0f, null)

        return combinedBitmap
    }

}