package com.tokopedia.stories.creation.view.model

import java.net.URLConnection

/**
 * Created By : Jonathan Darwin on September 27, 2023
 */
enum class StoriesMediaType(val value: String) {
    Image("image"),
    Video("video"),
    Unknown("");

    companion object {
        fun parse(path: String): StoriesMediaType {
            val fileNameMap = URLConnection.getFileNameMap()
            val type = fileNameMap.getContentTypeFor(path)

            return values().find { type.startsWith(it.value) } ?: Unknown
        }
    }
}
