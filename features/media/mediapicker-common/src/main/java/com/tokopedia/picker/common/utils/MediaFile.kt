package com.tokopedia.picker.common.utils

import java.io.File

@JvmInline
value class MediaFile(val path: String?) {

    fun isVideo() = path?.let {
        isVideoFormat(it)
    } ?: false

    fun isImage() = path?.let {
        isImageFormat(it)
    } ?: false

    fun exist() = File(path).exists()
}
