package com.tokopedia.mediauploader.common.compressor

interface CompressionProgressListener {
    fun onProgressChanged(percent: Float)
}
