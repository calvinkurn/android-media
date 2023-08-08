package com.tokopedia.stories.usecase.response

import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 08/08/23
 */
data class UpdateStoryResponse(
    @SerializedName("contentStoryUpdateStory")
    val storyId: StoryId = StoryId(),
) {

    data class StoryId(
        @SerializedName("storyID")
        val storyId: String = "",
    )
}
