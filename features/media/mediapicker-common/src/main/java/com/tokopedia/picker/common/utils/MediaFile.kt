package com.tokopedia.picker.common.utils

import java.io.File

@JvmInline
value class MediaFile(val path: String) {

    fun isVideo() = isVideoFormat(path)

    fun isImage() = isImageFormat(path)

    fun exist() = File(path).exists()
}
