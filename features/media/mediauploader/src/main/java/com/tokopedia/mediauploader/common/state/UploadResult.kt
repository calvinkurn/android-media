package com.tokopedia.mediauploader.common.state

sealed class UploadResult {
    class Success(val uploadId: String): UploadResult()
    class Error(val message: String): UploadResult()
}