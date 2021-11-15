package com.tokopedia.mediauploader.common.state

interface ProgressCallback {
    fun onProgress(percentage: Int)
}