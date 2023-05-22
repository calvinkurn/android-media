package com.tokopedia.mediauploader.common.internal.compressor.data

data class Result(
    val success: Boolean,
    val failureMessage: String?,
    val size: Long = 0,
    val path: String? = null,
)
