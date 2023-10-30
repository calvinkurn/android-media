package com.tokopedia.editor.data.repository

import android.graphics.Bitmap
import com.tokopedia.editor.analytics.EditorLogger
import com.tokopedia.editor.data.model.CanvasSize
import com.tokopedia.utils.image.ImageProcessingUtil.getTokopediaPhotoPath
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

interface ImageSaveRepository {

    /**
     * Save bitmap without specify the output path result,
     *
     * the file path will be generated using built-in tokopedia core util.
     */
    fun saveBitmap(bitmap: Bitmap): String

    /**
     * Save bitmap with customize the target output path.
     */
    fun saveBitmap(bitmap: Bitmap, outputPath: String): String

    /**
     * Generate and save scaled bitmap with width and height param with generated target path.
     */
    fun saveBitmap(bitmap: Bitmap, size: CanvasSize): String
}

class ImageSaveRepositoryImpl @Inject constructor() : ImageSaveRepository {

    private val compressFormat = Bitmap.CompressFormat.PNG

    override fun saveBitmap(bitmap: Bitmap, outputPath: String): String {
        val file = File(outputPath)
        if (file.exists()) file.delete()

        try {
            val out = FileOutputStream(file)
            bitmap.compress(compressFormat, IMAGE_QUALITY, out)
            bitmap.recycle()
            out.close()
        } catch (t: Throwable) {
            EditorLogger.saveBitmap(outputPath, t.stackTraceToString())
            return ""
        }

        return file.path
    }

    override fun saveBitmap(bitmap: Bitmap): String {
        val file = getTokopediaPhotoPath(compressFormat, DEFAULT_CACHE_FOLDER)
        return saveBitmap(bitmap, file.path)
    }

    override fun saveBitmap(bitmap: Bitmap, size: CanvasSize): String {
        val file = getTokopediaPhotoPath(compressFormat, DEFAULT_CACHE_FOLDER)
        val newBitmap = Bitmap.createScaledBitmap(bitmap, size.width, size.height, true)
        return saveBitmap(newBitmap, file.path)
    }

    companion object {
        private const val DEFAULT_CACHE_FOLDER = "Tokopedia/"

        private const val IMAGE_QUALITY = 100
    }
}
