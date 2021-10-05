package com.tokopedia.mediauploader.data.params

import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.util.UploadRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File

data class VideoUploaderParam(
    override var sourceId: String = "",
    override var uploadUrl: String = "",
    override var filePath: String = "",
    override var timeOut: String = ""
) : CommonParam, SourceIdParam, ParamValidator() {

    private val file by lazy { File(filePath) }

    fun videoBody(
        progressCallback: ProgressCallback?
    ): MultipartBody {

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        val contentType = MediaType.parse(SUPPORTED_CONTENT_TYPE)
        val requestBody = UploadRequestBody(file, contentType, progressCallback)

        builder.addFormDataPart(BODY_FILE_BLOB, file.name, requestBody)
        builder.addFormDataPart(BODY_FILE_NAME, file.name, requestBody)

        return builder.build()
    }

    companion object {
        private const val BODY_FILE_NAME = "file_name"
        private const val BODY_FILE_BLOB = "file"

        private const val SUPPORTED_CONTENT_TYPE = "video/*"

        fun create(
            fileToUpload: File,
            policy: SourcePolicy,
            sourceId: String
        ) = VideoUploaderParam().apply {
            uploadUrl = "https://vod.tokopedia.com/v2/video/upload/simple/$sourceId"
            timeOut = policy.timeOut.toString()
            filePath = fileToUpload.path
        }

    }

}