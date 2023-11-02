package com.tokopedia.stories.creation.view.model

/**
 * Created By : Jonathan Darwin on October 23, 2023
 */
data class StoriesMedia(
    val filePath: String,
    val type: StoriesMediaType,
) {
    companion object {
        val Empty: StoriesMedia
            get() = StoriesMedia(
                filePath = "",
                type  = StoriesMediaType.Unknown,
            )
    }
}
