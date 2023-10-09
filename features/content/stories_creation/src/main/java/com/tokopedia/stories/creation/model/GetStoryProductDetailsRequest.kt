package com.tokopedia.stories.creation.model

/**
 * Created By : Jonathan Darwin on October 09, 2023
 */
data class GetStoryProductDetailsRequest(
    val storyId: String
) {

    fun buildRequestParam(): Map<String, Any> {
        return mapOf(
            PARAM_REQ to mapOf(
                PARAM_STORY_ID to storyId,
            )
        )
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_STORY_ID = "storyID"
    }
}

