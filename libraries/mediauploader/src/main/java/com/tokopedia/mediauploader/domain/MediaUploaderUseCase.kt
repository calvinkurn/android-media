package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.util.ProgressRequestBody
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

open class MediaUploaderUseCase @Inject constructor(
        private val services: UploaderServices
) : UseCase<MediaUploader>() {

    var requestParams = RequestParams()
    lateinit var progressCallback: ProgressCallback

    override suspend fun executeOnBackground(): MediaUploader {
        if (requestParams.parameters.isEmpty()) throw Exception("Not param found")
        val uploadUrl = requestParams.getString(PARAM_URL_UPLOAD, "")
        val filePath = requestParams.getString(BODY_FILE_UPLOAD, "")
        val params = createParam(uploadUrl, filePath)
        return services.uploadFile(uploadUrl, fileUploadParam(params, progressCallback))
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val PARAM_URL_UPLOAD = "url"

        private const val DEFAULT_FILE_TYPE = "image"

        private fun createBodyParam(filePath: String): Map<String, Any> {
            val requestParams = hashMapOf<String, Any>()
            requestParams[BODY_FILE_UPLOAD] = filePath
            return requestParams
        }

        private fun fileUploadParam(
                requestParams: RequestParams,
                progressCallback: ProgressCallback
        ): MultipartBody.Part {
            val file = File(requestParams.getString(BODY_FILE_UPLOAD, ""))
            val contentType = MediaType.parse("$DEFAULT_FILE_TYPE/*")
            val requestBody = ProgressRequestBody(file, contentType, progressCallback)
            return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, file.name, requestBody)
        }

        fun createParam(uploadUrl: String, filePath: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_URL_UPLOAD, uploadUrl)
            requestParams.putAll(createBodyParam(filePath))
            return requestParams
        }
    }

}