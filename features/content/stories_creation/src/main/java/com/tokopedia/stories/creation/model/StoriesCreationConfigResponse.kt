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

    @SerializedName("min_video_duration")
    val minVideoDuration: Long = 0L,

    @SerializedName("max_video_duration")
    val maxVideoDuration: Long = 0L,

    @SerializedName("min_product_tag")
    val minProductTag: Int = 0,

    @SerializedName("max_product_tag")
    val maxProductTag: Int = 0,

    @SerializedName("show_duration")
    val storyDuration: String = "",

    @SerializedName("image_source_id")
    val imageSourceId: String = "",

    @SerializedName("video_source_id")
    val videoSourceId: String = "",

    @SerializedName("story_applink")
    val storyApplink: String = "",
) {

    data class MaxStoryBottomSheetConfig(
        @SerializedName("is_max_story_reached")
        val isMaxStoryReached: Boolean = false,

        @SerializedName("image_url")
        val imageUrl: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("description")
        val description: String = "",

        @SerializedName("create_button_copy")
        val createButtonCopy: String = "",

        @SerializedName("back_button_copy")
        val backButtonCopy: String = "",
    )
}
