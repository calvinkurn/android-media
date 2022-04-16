package com.tokopedia.media.picker.helper.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream

object ImageGenerator {

    private const val DEFAULT_SIZE = 400
    private const val IMAGES_FILE_COUNT = 5

    fun generateImages(context: Context) {
        (0 until IMAGES_FILE_COUNT)
            .map { fileName(it) }
            .forEachIndexed { index, path ->
                val dir = context.externalCacheDir
                val file = File(dir, path)

                if (file.exists().not()) file.createNewFile()

                generateBitmapFile(file, index.toString())
                addImageToGallery(context.contentResolver, file)
        }
    }

    fun getFiles(context: Context): List<File> {
        return (0 until IMAGES_FILE_COUNT).map {
            File(context.externalCacheDir, fileName(it))
        }
    }

    private fun fileName(num: Int): String {
        val numberFormat = num.toString().padStart(5, '0')
        return "test_mp_images_${numberFormat}.jpg"
    }

    private fun addImageToGallery(cr: ContentResolver, imageFile: File): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, imageFile.name)
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.DATA, imageFile.path)
        }
        return cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
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