package com.tokopedia.media.picker.helper.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream

object ImageGenerator {

    private const val DEFAULT_SIZE = 400
    const val IMAGES_FILES_COUNT = 5

    fun getFiles(context: Context): List<File> {
        val files = mutableListOf<File>()
        (0 until IMAGES_FILES_COUNT)
            .map { fileName(it) }
            .forEachIndexed { index, path ->
                val dir = context.externalCacheDir
                val file = File(dir, path)

                if (file.exists().not()) file.createNewFile()
                generateBitmapFile(file, index.toString())

                files.add(file)
        }

        return files
    }

    private fun fileName(num: Int): String {
        val numberFormat = num.toString().padStart(5, '0')
        return "test_mp_images_${numberFormat}.jpg"
    }

    @Throws(IOException::class)
    private fun generateBitmapFile(
        file: File,
        identifier: String,
        width: Int = DEFAULT_SIZE,
        height: Int = DEFAULT_SIZE
    ) {
        val stream = file.outputStream()
        val bmp = createBitmap(identifier, width, height)

        writeBitmap(stream, bmp)
    }

    private fun createBitmap(identifier: String, width: Int, height: Int): Bitmap {
        val conf = Bitmap.Config.ARGB_8888
        val bmp = Bitmap.createBitmap(width, height, conf)

        val canvas = Canvas(bmp)

        val paint = Paint()
        paint.color = Color.GRAY

        val widthPart = width / 10
        val heightPart = height / 10

        canvas.drawRect(
            widthPart.toFloat(),
            heightPart.toFloat(),
            (width - widthPart).toFloat(),
            (height - heightPart).toFloat(),
            paint
        )

        val textPaint = Paint()
        textPaint.color = Color.BLACK
        textPaint.textAlign = Paint.Align.CENTER

        val xPos = canvas.width / 2
        val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2).toInt()

        canvas.drawText(identifier, xPos.toFloat(), yPos.toFloat(), textPaint)

        canvas.save()

        return bmp
    }

    @Throws(IOException::class)
    private fun writeBitmap(stream: OutputStream, bmp: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bitmapData = byteArrayOutputStream.toByteArray()
        stream.write(bitmapData)
        stream.flush()
        stream.close()
    }

}