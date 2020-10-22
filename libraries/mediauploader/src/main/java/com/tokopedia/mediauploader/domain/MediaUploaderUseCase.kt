package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.base.BaseUseCase
import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.entity.MediaUploader
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.util.UploadRequestBody
import com.tokopedia.usecase.RequestParams
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.createFormData
import java.io.File
import javax.inject.Inject
import okhttp3.MediaType.parse as mediaTypeParse

open class MediaUploaderUseCase @Inject constructor(
        private val services: UploaderServices
) : BaseUseCase<RequestParams, MediaUploader>() {

    var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: RequestParams): MediaUploader {
        if (params.parameters.isEmpty()) throw Exception("Not param found")

        val uploadUrl = params.getString(PARAM_URL_UPLOAD, "")
        val filePath = params.getString(BODY_FILE_UPLOAD, "")
        val timeOut = params.getString(PARAM_TIME_OUT, "")

        val file = fileParam(filePath, progressCallback)
        return services.uploadFile(uploadUrl, file, timeOut)
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val PARAM_URL_UPLOAD = "url"
        private const val PARAM_TIME_OUT = "timeout"

        private fun fileParam(
                filePath: String,
                progressCallback: ProgressCallback?
        ): MultipartBody.Part {
            val file = File(filePath)
            val contentType = mediaTypeParse("image/*")
            val requestBody = UploadRequestBody(file, contentType, progressCallback)
            return createFormData(BODY_FILE_UPLOAD, file.name, requestBody)
        }

        fun createParams(
                uploadUrl: String,
                filePath: String,
                timeOut: String
        ): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_URL_UPLOAD, uploadUrl)
                putString(BODY_FILE_UPLOAD, filePath)
                putString(PARAM_TIME_OUT, timeOut)
            }
        }
    }

}