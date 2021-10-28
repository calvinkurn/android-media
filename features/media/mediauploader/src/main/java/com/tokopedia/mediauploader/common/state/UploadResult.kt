package com.tokopedia.mediauploader.common.state

sealed class UploadResult {
    class Success(
        // image upload result
        val uploadId: String = "",

        // video upload result
        val videoUrl: String = ""
    ): UploadResult()

    class Error(val message: String): UploadResult()
}