package com.tokopedia.mediauploader.common.state

sealed class ProgressType {
    object Compression: ProgressType()
    object Upload: ProgressType()
}

interface ProgressUploader {
    fun onProgress(percentage: Int, type: ProgressType)
}
