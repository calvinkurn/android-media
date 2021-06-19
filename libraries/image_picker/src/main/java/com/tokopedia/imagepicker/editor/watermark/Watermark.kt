package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import android.widget.ImageView
import com.tokopedia.imagepicker.editor.watermark.entity.Image
import com.tokopedia.imagepicker.editor.watermark.entity.Text
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.resizeBitmap
import com.tokopedia.imagepicker.editor.watermark.utils.BitmapHelper.textAsBitmap

data class Watermark(
    var context: Context,
    var backgroundImg: Bitmap? = null,
    var watermarkImg: Image? = null,
    var watermarkBitmaps: List<Image>,
    var watermarkText: Text? = null,
    var watermarkTexts: List<Text>,
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
            createWatermarkImages(watermarkBitmaps)
            createWatermarkText(watermarkText)
            createWatermarkTexts(watermarkTexts)
        } else {
            createWatermarkImageAndText(watermarkImg, watermarkText)
        }
    }

    private fun createWatermarkImageAndText(watermarkImg: Image?, watermarkText: Text?) {
        if (backgroundImg == null || watermarkImg == null || watermarkText == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermarkImg.alpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)

        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        val logoBitmap = watermarkImg.image!!.resizeBitmap(
            size = watermarkImg.size.toFloat(),
            background = backgroundImg!!
        )

        val textBitmap = watermarkText.textAsBitmap(context)

        var scaledWatermarkBitmap = combineImages(logoBitmap, textBitmap)

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

    private fun createWatermarkImage(watermarkImg: Image?) {
        if (backgroundImg == null || watermarkImg == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermarkImg.alpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)

        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        var scaledWatermarkBitmap = watermarkImg.image!!.resizeBitmap(
            size = watermarkImg.size.toFloat(),
            background = backgroundImg!!
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

    private fun createWatermarkImages(watermarkImages: List<Image>) {
        watermarkImages.forEach {
            createWatermarkImage(it)
        }
    }

    private fun createWatermarkText(watermarkText: Text?) {
        if (backgroundImg == null || watermarkText == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermarkText.textAlpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)
        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        var scaledWatermarkBitmap = watermarkText.textAsBitmap(context)

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

    private fun createWatermarkTexts(watermarkTexts: List<Text>) {
        watermarkTexts.forEach {
            createWatermarkText(it)
        }
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

    private fun combineImages(
        c: Bitmap,
        s: Bitmap
    ): Bitmap {
        val cs: Bitmap?
        val width: Int
        val height: Int

        if (c.width > s.width) {
            width = (c.width + s.width) * 2
            height = c.height
        } else {
            width = (s.width + s.width) * 2
            height = c.height
        }

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val comboImage = Canvas(cs)

        comboImage.drawBitmap(c, 0f, 0f, null)
        comboImage.drawBitmap(s, c.width.toFloat() * 2, 0f, null)

        return cs
    }

}