package com.tokopedia.imagepicker.editor.watermark

import android.content.Context
import android.graphics.*
import android.widget.ImageView
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
        if (backgroundImg == null || watermark == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermark.alpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)

        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        val logoBitmap = watermark.image!!.resizeBitmap(
            size = watermark.imageSize.toFloat(),
            background = backgroundImg!!
        )

        val textBitmap = watermark.text.textAsBitmap(context, watermark)

        var scaledWatermarkBitmap = combineImages(logoBitmap, textBitmap)

        scaledWatermarkBitmap = adjustPhotoRotation(
            scaledWatermarkBitmap,
            watermark.position.rotation.toInt()
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
                (watermark.position.positionX * backgroundImg!!.width).toFloat(),
                (watermark.position.positionY * backgroundImg!!.height).toFloat(),
                watermarkPaint
            )
        }

        canvasBitmap = newBitmap
        outputImage = newBitmap
    }

    private fun createWatermarkImage(watermarkImg: ImageUIModel?) {
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
            size = watermarkImg.imageSize.toFloat(),
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

    private fun createWatermarkText(watermarkText: TextUIModel?) {
        if (backgroundImg == null || watermarkText == null) {
            return
        }

        val watermarkPaint = Paint()
        watermarkPaint.alpha = watermarkText.alpha

        val newBitmap = Bitmap.createBitmap(
            backgroundImg!!.width,
            backgroundImg!!.height,
            backgroundImg!!.config
        )

        val watermarkCanvas = Canvas(newBitmap)
        canvasBitmap?.let {
            watermarkCanvas.drawBitmap(it, 0f, 0f, null)
        }

        var scaledWatermarkBitmap = watermarkText.text.textAsBitmap(context, watermarkText)

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