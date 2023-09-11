package com.tokopedia.stories.creation.model

import com.tokopedia.content.common.types.ContentCommonUserType

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class CreateStoryRequest(
    val authorId: String,
    val authorType: String,
) {

    fun buildRequestParam(): Map<String, Any> {
        return mapOf(
            PARAM_REQ to mapOf(
                PARAM_AUTHOR_ID to authorId,
                PARAM_AUTHOR_TYPE to when (authorType) {
                    ContentCommonUserType.TYPE_USER -> ContentCommonUserType.VALUE_TYPE_ID_USER
                    ContentCommonUserType.TYPE_SHOP -> ContentCommonUserType.VALUE_TYPE_ID_SHOP
                    else -> 0
                }
            )
        )
    }

    companion object {
        private const val PARAM_REQ = "req"
        private const val PARAM_AUTHOR_ID = "authorID"
        private const val PARAM_AUTHOR_TYPE = "authorType"
    }
}
