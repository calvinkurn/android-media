package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.BaseUseCase
import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.util.UploadRequestBody
import com.tokopedia.usecase.RequestParams
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

open class MediaUploaderUseCase @Inject constructor(
        private val services: UploaderServices
) : BaseUseCase<RequestParams, MediaUploader>() {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: RequestParams): MediaUploader {
        if (params.parameters.isEmpty()) throw Exception("Not param found")
        val uploadUrl = params.getString(PARAM_URL_UPLOAD, "")
        val filePath = params.getString(BODY_FILE_UPLOAD, "")
        val paramBuilder = createParams(uploadUrl, filePath)
        return services.uploadFile(uploadUrl, fileParam(paramBuilder, progressCallback))
    }

    companion object {
        /**
         * keys of params
         * @param url
         * @param file_upload*
         * file_upload: also use it on body of multi part
         */
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val PARAM_URL_UPLOAD = "url"

        /**
         * default media type
         */
        private const val DEFAULT_FILE_TYPE = "image"

        private fun bodyParam(filePath: String): Map<String, Any> {
            val requestParams = hashMapOf<String, Any>()
            requestParams[BODY_FILE_UPLOAD] = filePath
            return requestParams
        }

        private fun fileParam(
                requestParams: RequestParams,
                progressCallback: ProgressCallback?
        ): MultipartBody.Part {
            val file = File(requestParams.getString(BODY_FILE_UPLOAD, ""))
            val contentType = MediaType.parse("$DEFAULT_FILE_TYPE/*")
            val requestBody = UploadRequestBody(file, contentType, progressCallback)
            return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, file.name, requestBody)
        }

        fun createParams(uploadUrl: String, filePath: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_URL_UPLOAD, uploadUrl)
            requestParams.putAll(bodyParam(filePath))
            return requestParams
        }
    }

}