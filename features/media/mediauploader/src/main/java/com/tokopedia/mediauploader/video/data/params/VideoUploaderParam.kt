package com.tokopedia.mediauploader.video.data.params

import com.tokopedia.mediauploader.common.data.params.CommonParam
import com.tokopedia.mediauploader.common.data.params.ParamValidator
import com.tokopedia.mediauploader.common.data.params.SourceIdParam
import com.tokopedia.mediauploader.common.util.UploadRequestBody
import com.tokopedia.mediauploader.data.state.ProgressCallback
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

    fun videoFileName(): String = file.name

    fun videoBody(progressCallback: ProgressCallback?): MultipartBody.Part {
        val contentType = MediaType.parse(SUPPORTED_CONTENT_TYPE)
        val requestBody = UploadRequestBody(file, contentType, progressCallback)
        return MultipartBody.Part.createFormData(BODY_FILE_BLOB, file.name, requestBody)
    }

    companion object {
        private const val BODY_FILE_NAME = "file_name"
        private const val BODY_FILE_BLOB = "file"

        private const val SUPPORTED_CONTENT_TYPE = "video/*"

        fun create(
            fileToUpload: String,
            timeOutUpload: String,
            sourceIdUpload: String
        ) = VideoUploaderParam().apply {
            uploadUrl = "https://vod.tokopedia.com/v2/video/upload/simple/$sourceIdUpload"
            timeOut = timeOutUpload
            filePath = fileToUpload
            sourceId = sourceIdUpload
        }

    }

}