package com.tokopedia.mediauploader.data.params

import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.util.UploadRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File

data class MediaUploaderParam(
    var uploadUrl: String = "",
    var filePath: String = "",
    var timeOut: String = ""
) {

    fun hasEmptyParams(): Boolean {
        return uploadUrl.isEmpty() && filePath.isEmpty() && timeOut.isEmpty()
    }

    fun imageUploaderParam(
        filePath: String,
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
    }

}