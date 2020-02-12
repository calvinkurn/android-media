package com.tokopedia.mediauploader.data.entity

data class Uploader(
        val isSuccess: Boolean = false,
        val message: String = "",
        val uploadId: String = ""
)