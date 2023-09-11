package com.tokopedia.stories.creation.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class StoriesCreationConfigResponse(
    @SerializedName("max_story_bottomsheet_config")
    val maxStoryBottomSheetConfig: MaxStoryBottomSheetConfig = MaxStoryBottomSheetConfig(),

    @SerializedName("draft_story_id")
    val draftStoryId: String = "",
) {

    data class MaxStoryBottomSheetConfig(
        @SerializedName("is_limit_reached")
        val isLimitReached: Boolean = false,

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("active_button_copy")
        val activeButtonCopy: String = "",

        @SerializedName("button_copy")
        val buttonCopy: String = "",
    )
}
