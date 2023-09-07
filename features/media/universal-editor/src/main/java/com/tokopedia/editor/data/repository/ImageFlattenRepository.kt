package com.tokopedia.editor.data.repository

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.tokopedia.editor.util.getEditorCacheFolderPath
import com.tokopedia.utils.file.FileUtil
import javax.inject.Inject

interface ImageFlattenRepository {
    fun flattenImage(imageBitmap: Bitmap, textBitmap: Bitmap?): String
}

class ImageFlattenRepositoryImpl @Inject constructor(
    private val imageSaveRepository: ImageSaveRepository
) : ImageFlattenRepository {
    override fun flattenImage(imageBitmap: Bitmap, textBitmap: Bitmap?): String {
        val flattenBitmap = Bitmap.createBitmap(imageBitmap.width, imageBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(flattenBitmap)

        canvas.drawRect(0f, 0f, flattenBitmap.width.toFloat(), flattenBitmap.height.toFloat(), Paint(Color.BLACK))
        canvas.drawBitmap(imageBitmap, 0f, 0f, null)

        textBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        val path = getEditorCacheFolderPath() + FileUtil.generateUniqueFileName() + FILENAME_SUFFIX
        return imageSaveRepository.saveBitmap(path, flattenBitmap)
    }

    companion object {
        private const val FILENAME_SUFFIX = "_result.png"
    }
}
