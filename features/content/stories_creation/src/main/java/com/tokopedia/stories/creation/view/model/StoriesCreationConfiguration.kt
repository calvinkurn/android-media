package com.tokopedia.stories.creation.view.model

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class StoriesCreationConfiguration(
    val storiesId: String,
    val maxProductTag: Int,
    val storyDuration: String,
    val minVideoDuration: Int,
    val maxVideoDuration: Int,
    val imageSourceId: String,
    val videoSourceId: String,
    val storiesApplinkTemplate: String,
    val maxStoriesConfig: MaxStoriesConfig,
) {

    data class MaxStoriesConfig(
        val isMaxStoryReached: Boolean,
        val imageUrl: String,
        val title: String,
        val description: String,
        val primaryText: String,
        val secondaryText: String,
    ) {
        companion object {
            val Empty: MaxStoriesConfig
                get() = MaxStoriesConfig(
                    isMaxStoryReached = false,
                    imageUrl = "",
                    title = "",
                    description = "",
                    primaryText = "",
                    secondaryText = "",
                )
        }
    }

    companion object {
        val Empty: StoriesCreationConfiguration
            get() = StoriesCreationConfiguration(
                storiesId = "",
                maxProductTag = 0,
                storyDuration = "",
                minVideoDuration = 0,
                maxVideoDuration = 0,
                imageSourceId = "",
                videoSourceId = "",
                maxStoriesConfig = MaxStoriesConfig.Empty,
                storiesApplinkTemplate = "",
            )
    }
}
