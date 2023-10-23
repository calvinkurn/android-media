package com.tokopedia.stories.creation.view.model

import com.tokopedia.creation.common.upload.model.ContentMediaType

/**
 * Created By : Jonathan Darwin on October 23, 2023
 */
data class StoriesMedia(
    val filePath: String,
    val type: ContentMediaType,
) {
    companion object {
        val Empty: StoriesMedia
            get() = StoriesMedia(
                filePath = "",
                type  = ContentMediaType.Unknown,
            )
    }
}
