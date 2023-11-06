package com.tokopedia.creation.common.upload.model

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
enum class UploadQueueStatus(val value: String) {
    Queued("queued"),
    InProcess("in_process"),
    Failed("failed"),
    Unknown("unknown");

    companion object {
        fun mapFromValue(value: String): UploadQueueStatus {
            return UploadQueueStatus.values()
                .find { it.value == value } ?: Unknown
        }
    }
}
