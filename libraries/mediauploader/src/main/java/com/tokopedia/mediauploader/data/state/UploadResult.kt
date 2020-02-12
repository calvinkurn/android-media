package com.tokopedia.mediauploader.data.state

sealed class UploadResult {
    class Success(val uploadId: String): UploadResult()
    class Error(val reason: UploadState): UploadResult()
}