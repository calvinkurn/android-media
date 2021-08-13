package com.tokopedia.mediauploader.data.state

interface ProgressCallback {
    fun onProgress(percentage: Int)
}