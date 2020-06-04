package com.tokopedia.mediauploader.data.state

sealed class UploadResult {
    class Success(val uploadId: String): UploadResult()
    class Error(val reason: UploadErrorState, val message: String = ""): UploadResult()
}