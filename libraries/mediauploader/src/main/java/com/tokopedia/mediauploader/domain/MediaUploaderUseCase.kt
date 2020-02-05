package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.data.UploaderServices
import com.tokopedia.mediauploader.data.entity.Uploader
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

open class MediaUploaderUseCase @Inject constructor(
        private val services: UploaderServices
) : UseCase<Uploader>() {

    var requestParams = mapOf<String, Any>()

    override suspend fun executeOnBackground(): Uploader {
        if (requestParams.isEmpty()) throw Exception("Not param found")
        val uploadUrl = requestParams[PARAM_URL_UPLOAD] as String
        val filePath = requestParams[BODY_FILE_UPLOAD] as String
        return services.uploadFile(uploadUrl, fileUploadParam(createParam(filePath)))
    }

    companion object {
        private const val PARAM_URL_UPLOAD = "url"
        private const val BODY_FILE_UPLOAD = "file_upload"

        private const val DEFAULT_FILE_TYPE = "image"

        private fun createBodyParam(filePath: String): Map<String, Any> {
            val requestParams = hashMapOf<String, Any>()
            requestParams[BODY_FILE_UPLOAD] = filePath
            return requestParams
        }

        private fun fileUploadParam(requestParams: RequestParams): MultipartBody.Part {
            val file = File(requestParams.getString(BODY_FILE_UPLOAD, ""))
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("$DEFAULT_FILE_TYPE/*"), file)
            return MultipartBody.Part.createFormData(
                    BODY_FILE_UPLOAD,
                    file.name,
                    requestBody
            )
        }

        fun createParam(filePath: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putAll(createBodyParam(filePath))
            return requestParams
        }
    }

}