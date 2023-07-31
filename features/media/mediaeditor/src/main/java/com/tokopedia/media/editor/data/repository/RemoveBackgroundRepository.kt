package com.tokopedia.media.editor.data.repository

import android.net.Uri
import com.tokopedia.media.editor.data.EditorNetworkServices
import com.tokopedia.media.editor.utils.getEditorSaveFolderPath
import com.tokopedia.utils.image.ImageProcessingUtil
import com.tokopedia.utils.image.ImageProcessingUtil.getCompressFormat
import kotlinx.coroutines.flow.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

interface RemoveBackgroundRepository {
    suspend operator fun invoke(filePath: String): Flow<File?>
}

class RemoveBackgroundRepositoryImpl @Inject constructor(
    private val bitmapConverter: BitmapConverterRepository,
    private val services: EditorNetworkServices
) : RemoveBackgroundRepository {

    /**
     * Because of we need to passing a File to request body of remove-background,
     * First, we need to convert the image url/uri into bitmap.
     * After that, convert the bitmap into File.
     * Hence we can hit the remove background service.
     *
     * Since the remove-background returning as byteStream(),
     * we need to convert back the byteArray into File.
     */
    override suspend operator fun invoke(filePath: String): Flow<File?> {
        return flow {
            mapUriToFile(filePath)?.let {
                val fileBody = it.toMultiPartBody()
                val result = services.removeBackground(fileBody)

                emit(result)
            } ?: run {
                emit(null)
            }
        }.map { responseBody ->
            responseBody?.let {
                val compressFormat = filePath.getCompressFormat()
                ImageProcessingUtil.writeImageToTkpdPath(
                    it.byteStream(),
                    compressFormat,
                    directoryRelativePath = getEditorSaveFolderPath()
                )
            }
        }
    }

    private fun mapUriToFile(filePath: String): File? {
        val compressFormat = filePath.getCompressFormat()
        val bitmap = bitmapConverter.uriToBitmap(Uri.parse(filePath)) ?: return null

        return ImageProcessingUtil.writeImageToTkpdPath(bitmap, compressFormat)
    }

    private fun File.toMultiPartBody(): MultipartBody.Part {
        val contentType = SUPPORTED_CONTENT_TYPE.toMediaType()
        val requestBody = this.asRequestBody(contentType)

        return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, this.name, requestBody)
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"
    }

}
