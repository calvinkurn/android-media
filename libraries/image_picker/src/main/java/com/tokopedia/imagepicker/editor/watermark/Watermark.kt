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
import com.tokopedia.imagepicker.editor.watermark.utils.createBitmap

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
        // set the initial value of canvas
        canvasBitmap = backgroundImg

        // set the initial value of watermark result
        outputImage = backgroundImg

        if (!isCombine) {
            createWatermarkImage(watermarkImg)
            createWatermarkText(watermarkText)
        } else {
            createWatermarkTextAndImage(watermarkTextAndImage)
        }
    }

    /**
     * watermark builder by [TextUIModel] and [ImageUIModel]
     * @param watermark
     */
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

    /**
     * watermark builder by image
     * @param watermarkImage
     */
    private fun createWatermarkImage(watermarkImage: ImageUIModel?) {
        if (watermarkImage == null) return

        createWatermark(
            bitmap = watermarkImage.image!!.resizeBitmap(
                size = watermarkImage.imageSize.toFloat(),
                background = backgroundImg!!
            ),
            config = watermarkImage
        )
    }

    /**
     * watermark builder by text
     * @param watermarkText
     */
    private fun createWatermarkText(watermarkText: TextUIModel?) {
        if (watermarkText == null) return

        createWatermark(
            bitmap = watermarkText.text
                .textAsBitmap(context, watermarkText)
                .combine(null),
            config = watermarkText
        )
    }

    /**
     * generic function for watermark builder
     * @param bitmap (Bitmap)
     * @param config (Generic)
     */
    private fun createWatermark(
        bitmap: Bitmap,
        config: BaseWatermark?
    ) {
        if (config == null) return

        val paint = Paint().apply {
            alpha = config.alpha
        }

        backgroundImg?.let {
            val newBitmap = createBitmap(it.width, it.height, it.config)
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

    /**
     * set the watermark result into image view
     * @param target (ImageView)
     */
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

        return bitmap.createBitmap(matrix = matrix)
    }

    private fun Bitmap.combine(other: Bitmap?): Bitmap {
        val width: Int
        val height: Int
        val spaceThreshold = 2

        val secondBitmap = other ?: createBitmap(0, 0)

        if (this.width > secondBitmap.width) {
            width = (this.width + secondBitmap.width) * spaceThreshold
            height = this.height
        } else {
            width = (secondBitmap.width + secondBitmap.width) * spaceThreshold
            height = this.height
        }

        val combinedBitmap = createBitmap(width, height)
        val combinedCanvas = Canvas(combinedBitmap)

        combinedCanvas.drawBitmap(this, 0f, 0f, null)
        combinedCanvas.drawBitmap(secondBitmap, this.width.toFloat() * spaceThreshold, 0f, null)

        return combinedBitmap
    }

}