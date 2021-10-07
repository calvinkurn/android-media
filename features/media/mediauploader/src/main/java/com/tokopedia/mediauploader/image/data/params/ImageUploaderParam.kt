package com.tokopedia.mediauploader.image.data.params

import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.data.params.CommonParam
import com.tokopedia.mediauploader.common.data.params.ParamValidator
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.common.util.UploadRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File

data class ImageUploaderParam(
    override var uploadUrl: String = "",
    override var filePath: String = "",
    override var timeOut: String = ""
) : CommonParam, ParamValidator() {

    fun imageBody(
        progressCallback: ProgressCallback?
    ): MultipartBody.Part {
        val file = File(filePath)
        val contentType = MediaType.parse(SUPPORTED_CONTENT_TYPE)
        val requestBody = UploadRequestBody(file, contentType, progressCallback)
        return MultipartBody.Part.createFormData(BODY_FILE_UPLOAD, file.name, requestBody)
    }

    companion object {
        private const val BODY_FILE_UPLOAD = "file_upload"
        private const val SUPPORTED_CONTENT_TYPE = "image/*"

        fun imageUploadUrl(baseUrl: String, sourceId: String): String {
            return "$baseUrl/v1/upload/image/$sourceId"
        }

        fun create(
            fileToUpload: File,
            policy: SourcePolicy,
            sourceId: String
        ) = ImageUploaderParam().apply {
            uploadUrl = imageUploadUrl(policy.host, sourceId)
            timeOut = policy.timeOut.toString()
            filePath = fileToUpload.path
        }

    }

}