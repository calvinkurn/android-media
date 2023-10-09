package com.tokopedia.creation.common.upload.model.dto.stories

import com.tokopedia.creation.common.upload.model.stories.StoriesStatus

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
data class StoriesUpdateStoryRequest(
    val storyId: String,
    val activeMediaId: String,
    val status: StoriesStatus,
) {

    fun buildRequestParam(): Map<String, Any> {
        return mapOf(
            "req" to mapOf(
                "storyID" to storyId,
                "activeMediaID" to activeMediaId,
                "status" to status.value,
            )
        )
    }
}
