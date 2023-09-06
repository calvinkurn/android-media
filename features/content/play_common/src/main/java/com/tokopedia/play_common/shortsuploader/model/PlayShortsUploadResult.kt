package com.tokopedia.play_common.shortsuploader.model

/**
 * Created By : Jonathan Darwin on August 23, 2023
 */
sealed interface PlayShortsUploadResult {

    object Unknown : PlayShortsUploadResult

    data class Success(
        val progress: Int,
        val data: PlayShortsUploadModel
    ) : PlayShortsUploadResult
}
