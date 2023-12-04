package com.tokopedia.creation.common.upload.model

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
enum class CreationUploadType(val type: String) {
    Post("post"),
    Shorts("shorts"),
    Stories("stories"),
    Unknown("unknown");

    companion object {
        fun mapFromValue(value: String): CreationUploadType {
            return values().find { it.type == value } ?: Unknown
        }
    }
}
