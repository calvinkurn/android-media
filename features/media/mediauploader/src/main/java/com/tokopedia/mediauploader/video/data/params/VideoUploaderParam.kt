package com.tokopedia.mediauploader.video.data.params

import com.tokopedia.mediauploader.common.data.params.CommonParam
import com.tokopedia.mediauploader.common.data.params.ParamValidator
import com.tokopedia.mediauploader.common.data.params.SourceIdParam
import com.tokopedia.mediauploader.common.data.params.VideoLargeParam
import com.tokopedia.mediauploader.common.util.UploadRequestBody
import com.tokopedia.mediauploader.data.state.ProgressCallback
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

data class VideoUploaderParam(
    override var sourceId: String = "",
    override var uploadUrl: String = "",
    override var filePath: String = "",
    override var timeOut: String = "",
    override var partNumber: String = "",
    override var uploadId: String = ""
) : CommonParam, SourceIdParam, VideoLargeParam, ParamValidator() {

    val file by lazy { File(filePath) }

    inner class SingleUpload {
        fun fileName(): RequestBody {
            return RequestBody.create(MultipartBody.FORM, file.name)
        }

        fun fileBlob(progressCallback: ProgressCallback?): MultipartBody.Part {
            val contentType = MediaType.parse(SUPPORTED_CONTENT_TYPE)
            val requestBody = UploadRequestBody(file, contentType, progressCallback)
            return MultipartBody.Part.createFormData(BODY_FILE_BLOB, file.name, requestBody)
        }
    }

    inner class LargeUpload {}

    companion object {
        private const val BODY_FILE_BLOB = "file"
        private const val SUPPORTED_CONTENT_TYPE = "video/*"

        private const val BASE_VOD_UPLOAD_URL = "https://vod.tokopedia.com/v2"

        fun urlSimpleVideoUploadUrl(sourceId: String): String {
            return "$BASE_VOD_UPLOAD_URL/video/upload/simple/$sourceId"
        }

        fun urlInitLargeUploadUrl(): String {
            return "$BASE_VOD_UPLOAD_URL/video/upload/init"
        }

        fun urlCheckChunkLargeUploadUrl(): String {
            return "$BASE_VOD_UPLOAD_URL/video/upload/part"
        }

        fun create(
            fileToUpload: String = "",
            timeOutUpload: String = "",
            sourceIdUpload: String = "",
            partNumberChunks: String = "",
            currentUploadId: String = ""
        ) = VideoUploaderParam().apply {
            uploadUrl = urlSimpleVideoUploadUrl(sourceIdUpload)
            timeOut = timeOutUpload
            filePath = fileToUpload
            sourceId = sourceIdUpload
            partNumber = partNumberChunks
            uploadId = currentUploadId
        }

    }

}