package com.tokopedia.creation.common.upload.model

import java.net.URLConnection

/**
 * Created By : Jonathan Darwin on September 27, 2023
 */
enum class ContentMediaType(
    val value: String,
    val code: Int,
) {
    Image("image", 1),
    Video("video", 2),
    Unknown("", 0);

    val isImage: Boolean
        get() = this == Image

    val isVideo: Boolean
        get() = this == Video

    companion object {
        fun parse(code: Int): ContentMediaType {
            return values().find { it.code == code } ?: Unknown
        }
        fun parse(path: String): ContentMediaType {
            if (path.isEmpty()) return Unknown

            val fileNameMap = URLConnection.getFileNameMap()
            val type = fileNameMap.getContentTypeFor(path)

            return values().find { type.startsWith(it.value) } ?: Unknown
        }
    }
}
