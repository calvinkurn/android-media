package com.tokopedia.mediauploader.common.state

interface ProgressUploader {
    fun onProgress(percentage: Int, type: ProgressType)
}
