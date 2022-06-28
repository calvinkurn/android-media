package com.tokopedia.media.editor.data.repository

import android.net.Uri
import com.tokopedia.media.editor.data.EditorNetworkServices
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.image.ImageProcessingUtil.getCompressFormat
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

interface EditorRepository {
    suspend fun removeBackground(filePath: String): Flow<File?>
}

class EditorRepositoryImpl @Inject constructor(
    private val bitmapConverter: BitmapRepository,
    private val services: EditorNetworkServices
) : EditorRepository {

    override suspend fun removeBackground(filePath: String): Flow<File?> {
        return set(filePath).map {
            val compressFormat = filePath.getCompressFormat()
            ImageProcessingUtil.writeImageToTkpdPath(it.byteStream(), compressFormat)
        }
    }

    private suspend fun set(filePath: String): Flow<ResponseBody> {
        return flow {
            val compressFormat = filePath.getCompressFormat()
            val bitmap = bitmapConverter.uriToBitmap(Uri.parse(filePath))
            val file = ImageProcessingUtil.writeImageToTkpdPath(bitmap, compressFormat)

            if (file != null) {
                emitAll(services.removeBackground(file.file()))
            }
        }
    }

    private fun File.file(): MultipartBody.Part {
        val contentType = SUPPORTED_CONTENT_TYPE.toMediaType()
        val requestBody = this.asRequestBody(contentType)
        return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, this.name, requestBody)
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"
    }

}