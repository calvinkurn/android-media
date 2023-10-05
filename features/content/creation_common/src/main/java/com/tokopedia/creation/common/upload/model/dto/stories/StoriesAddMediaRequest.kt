package com.tokopedia.creation.common.upload.model.dto.stories

/**
 * Created By : Jonathan Darwin on October 02, 2023
 */
data class StoriesAddMediaRequest(
    val storyId: String,
    val type: Int,
    val mediaUrl: String,
    val coverUrl: String,
    val uploadId: String,
    val status: Int = 1,
    val orientation: Int = 0,
) {

    fun buildRequestParam(): Map<String, Any> {
        return mapOf(
            "req" to mapOf(
                "storyID" to storyId,
                "type" to type,
                "mediaURL" to mediaUrl,
                "coverURL" to coverUrl,
                "uploadID" to uploadId,
                "status" to status,
                "orientation" to orientation,
            )
        )
    }
}
