package com.tokopedia.creation.common.upload.model

/**
 * Created By : Jonathan Darwin on September 19, 2023
 */

sealed interface CreationUploadResult {

    object Unknown : CreationUploadResult

    object Empty : CreationUploadResult

    data class Upload(
        val data: CreationUploadData,
        val progress: Int,
    ) : CreationUploadResult

    data class OtherProcess(
        val data: CreationUploadData,
        val progress: Int,
    ) : CreationUploadResult

    data class Success(
        val data: CreationUploadData,
    ) : CreationUploadResult

    data class Failed(
        val data: CreationUploadData,
    ) : CreationUploadResult
}
