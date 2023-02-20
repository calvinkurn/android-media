package com.tokopedia.media.picker.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.picker.common.PICKER_URL_FILE_CODE
import com.tokopedia.utils.image.ImageProcessingUtil
import java.io.File
import javax.inject.Inject

interface BitmapConverterRepository {
    suspend fun convert(url: String): String?
}

class BitmapConverterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitmapConverterRepository {

    override suspend fun convert(url: String): String? {
        // 1. convert image to bitmap
        val bitmap = urlToBitmap(url)?: return null

        // 2. bitmap to file
        val file = ImageProcessingUtil.writeImageToTkpdPath(
            bitmap,
            Bitmap.CompressFormat.JPEG
        )

        file?.let {
            val fileName = getFileName(it.path)
            val newFilename = "$PICKER_URL_FILE_CODE$fileName"
            val newFile = File(it.path.replace(fileName, newFilename))

            it.renameTo(newFile)

            // 3. get file url, if image source from remote url
            return newFile.path
        }

        // 4. only if cache file is missing (failed to save bitmap #2)
        return null
    }

    private fun urlToBitmap(url: String): Bitmap? {
        return Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()
    }

    private fun getFileName(path: String): String {
        return path.substring(path.lastIndexOf("/") + 1)
    }

}
