package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkImage
import com.tokopedia.imagepicker.editor.watermark.uimodel.WatermarkText
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapUtils

data class WatermarkKt(
    var context: Context,
    var backgroundImg: Bitmap? = null,
    var watermarkImg: WatermarkImage? = null,
    var watermarkBitmaps: List<WatermarkImage>,
    var watermarkText: WatermarkText? = null,
    var watermarkTexts: List<WatermarkText>,
    var outputImage: Bitmap? = null,
    var canvasBitmap: Bitmap? = null,
    var isTitleMode: Boolean
) {

    init {
        canvasBitmap = backgroundImg
        outputImage = backgroundImg

        createWatermarkImage(watermarkImg)
        createWatermarkImages(watermarkBitmaps)
        createWatermarkText(watermarkText)
        createWatermarkTexts(watermarkTexts)
    }

    private fun createWatermarkImage(watermarkImg: WatermarkImage?) {
        if (backgroundImg == null && watermarkImg == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermarkImg!!.alpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)

        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        var scaledWatermarkBitmap = BitmapUtils.resizeBitmap(
            watermarkImg.image,
            watermarkImg.size.toFloat(),
            backgroundImg
        )

        scaledWatermarkBitmap = adjustPhotoRotation(
            scaledWatermarkBitmap,
            watermarkImg.position.rotation.toInt()
        )

        if (isTitleMode) {
            watermarkPaint.shader = BitmapShader(
                scaledWatermarkBitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
            )
            val bitmapShaderRect = watermarkCanvas.clipBounds
            watermarkCanvas.drawRect(bitmapShaderRect, watermarkPaint)
        } else {
            watermarkCanvas.drawBitmap(
                scaledWatermarkBitmap,
                (watermarkImg.position.positionX * backgroundImg!!.width).toFloat(),
                (watermarkImg.position.positionY * backgroundImg!!.height).toFloat(),
                watermarkPaint
            )
        }

        canvasBitmap = newBitmap
        outputImage = newBitmap
    }

    private fun createWatermarkImages(watermarkImages: List<WatermarkImage>) {
        watermarkImages.forEach {
            createWatermarkImage(it)
        }
    }

    private fun createWatermarkText(watermarkText: WatermarkText?) {
        if (backgroundImg == null && watermarkText == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermarkText!!.alpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)
        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        var scaledWatermarkBitmap = BitmapUtils.textAsBitmap(context, watermarkText)

        scaledWatermarkBitmap = adjustPhotoRotation(
            scaledWatermarkBitmap,
            watermarkText.position.rotation.toInt()
        )

        if (isTitleMode) {
            watermarkPaint.shader = BitmapShader(
                scaledWatermarkBitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT
            )
            val bitmapShaderRect = watermarkCanvas.clipBounds
            watermarkCanvas.drawRect(bitmapShaderRect, watermarkPaint)
        } else {
            watermarkCanvas.drawBitmap(
                scaledWatermarkBitmap,
                (watermarkText.position.positionX * backgroundImg!!.width).toFloat(),
                (watermarkText.position.positionY * backgroundImg!!.height).toFloat(),
                watermarkPaint
            )
        }

        canvasBitmap = newBitmap
        outputImage = newBitmap
    }

    private fun createWatermarkTexts(watermarkTexts: List<WatermarkText>) {
        watermarkTexts.forEach {
            createWatermarkText(it)
        }
    }

    fun saveToLocalPng(path: String) {
        BitmapUtils.saveAsPNG(outputImage, path, true)
    }

    fun setToImageView(target: ImageView) {
        target.setImageBitmap(outputImage)
    }

    private fun adjustPhotoRotation(bitmap: Bitmap, orientationAngle: Int): Bitmap {
        val matrix = Matrix()
        matrix.setRotate(
            orientationAngle.toFloat(),
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat()
        )

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

}