package com.tokopedia.creation.common.upload.model

/**
 * Created By : Jonathan Darwin on October 10, 2023
 */
enum class CreationUploadStatus(val value: String) {
    Unknown(""),
    Upload("upload"),
    OtherProcess("other_process"),
    Success("success"),
    Failed("failed");

    companion object {
        fun parse(value: String): CreationUploadStatus {
            return values().firstOrNull { it.value == value } ?: Unknown
        }
    }
}
