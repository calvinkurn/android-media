package com.tokopedia.stories.creation.view.model.activityResult

/**
 * Created By : Jonathan Darwin on November 01, 2023
 */
data class MediaPickerIntentData(
    val storiesId: String,
    val minVideoDuration: Int,
    val maxVideoDuration: Int,
    val previewActionText: String,
)
