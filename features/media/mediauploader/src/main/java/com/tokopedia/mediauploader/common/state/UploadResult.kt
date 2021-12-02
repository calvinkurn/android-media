package com.tokopedia.mediauploader.common.state

sealed class UploadResult {
    data class Success(
        // image upload result
        val uploadId: String = "",

        // video upload result
        val videoUrl: String = ""
    ): UploadResult()

    data class Error(val message: String): UploadResult()
}