package com.tokopedia.mediauploader.common.internal.compressor

interface CompressionProgressListener {
    fun onProgressChanged(percent: Float)
}
