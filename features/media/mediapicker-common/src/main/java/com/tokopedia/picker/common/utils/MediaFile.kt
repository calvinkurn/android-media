package com.tokopedia.picker.common.utils

@JvmInline
value class MediaFile(val path: String? = "") {

    fun isVideo() = isVideoFormat(path!!)
    fun isImage() = isImageFormat(path!!)
}
