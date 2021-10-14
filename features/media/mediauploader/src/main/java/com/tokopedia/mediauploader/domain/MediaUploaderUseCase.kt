package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.FileUploadServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.params.MediaUploaderParam
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.util.UploadRequestBody
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.createFormData
import java.io.File
import javax.inject.Inject
import okhttp3.MediaType.parse as mediaTypeParse

open class MediaUploaderUseCase @Inject constructor(
        private val services: FileUploadServices
) : CoroutineUseCase<MediaUploaderParam, MediaUploader>(Dispatchers.IO) {

    var progressCallback: ProgressCallback? = null

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: MediaUploaderParam): MediaUploader {
        if (params.hasEmptyParams()) throw RuntimeException("No param found")

        val file = fileParam(params.filePath, progressCallback)

        return services.uploadFile(
            urlToUpload = params.uploadUrl,
            timeOut = params.timeOut,
            fileUpload = file
        )
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"

        private fun fileParam(
                filePath: String,
                progressCallback: ProgressCallback?
        ): MultipartBody.Part {
            val file = File(filePath)
            val contentType = mediaTypeParse(SUPPORTED_CONTENT_TYPE)
            val requestBody = UploadRequestBody(file, contentType, progressCallback)
            return createFormData(BODY_FILE_UPLOAD, file.name, requestBody)
        }
    }

}