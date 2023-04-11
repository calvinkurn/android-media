package com.tokopedia.media.picker.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.media.picker.utils.internal.retryOperator
import com.tokopedia.picker.common.PICKER_URL_FILE_CODE
import com.tokopedia.utils.image.ImageProcessingUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

interface BitmapConverterRepository {
    fun convert(urls: List<String>): Flow<List<String?>>
}

class BitmapConverterRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BitmapConverterRepository {

    override fun convert(urls: List<String>): Flow<List<String?>> {
        return flow {
            val convertedUrl = urls.map {
                convert(it)
            }

            emit(convertedUrl)
        }
    }

    private suspend fun convert(url: String): String? {
        var bitmap: Bitmap? = null

        retryOperator(retries = 3) {
            try {
                val result = urlToBitmap(url)

                if (result != null) {
                    bitmap = result
                }
            } catch (ignored: Throwable) {
                operationFailed()
            }
        }

        val file = bitmap?.let {
            ImageProcessingUtil.writeImageToTkpdPath(
                it,
                Bitmap.CompressFormat.JPEG
            )
        }

        file?.let {
            val fileName = getFileName(it.path)
            val newFilename = "$PICKER_URL_FILE_CODE$fileName"
            val newFile = File(it.path.replace(fileName, newFilename))

            it.renameTo(newFile)

            // 3. get file url, if image source from remote url
            return newFile.path
        }

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
