package com.tokopedia.stories.creation.model

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class SetActiveProductTagRequest(
    val storyId: String,
    val productIds: List<String>
) {

    fun buildRequestParam(): Map<String, Any> {
        return mapOf(
            PARAM_REQ to mapOf(
                PARAM_STORY_ID to storyId,
                PARAM_PRODUCT_IDS to productIds,
            )
        )
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_STORY_ID = "storyID"
        private const val PARAM_PRODUCT_IDS = "productIDs"
    }
}
