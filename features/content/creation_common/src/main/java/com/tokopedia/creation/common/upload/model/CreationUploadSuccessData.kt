package com.tokopedia.creation.common.upload.model

/**
 * Created By : Jonathan Darwin on January 08, 2024
 */
sealed interface CreationUploadSuccessData {

    data class Post(
        val activityId: String,
    ) : CreationUploadSuccessData

    object Empty : CreationUploadSuccessData
}
