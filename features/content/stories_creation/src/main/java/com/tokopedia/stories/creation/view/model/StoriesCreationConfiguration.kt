package com.tokopedia.stories.creation.view.model

/**
 * Created By : Jonathan Darwin on September 11, 2023
 */
data class StoriesCreationConfiguration(
    val storiesId: String,
    val maxProductTag: Int,
    val showDuration: String,
    val maxStoriesConfig: MaxStoriesConfig,
) {

    data class MaxStoriesConfig(
        val isLimitReached: Boolean,
        val imageUrl: String,
        val title: String,
        val description: String,
        val primaryText: String,
        val secondaryText: String,
    ) {
        companion object {
            val Empty: MaxStoriesConfig
                get() = MaxStoriesConfig(
                    isLimitReached = false,
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
                showDuration = "",
                maxStoriesConfig = MaxStoriesConfig.Empty,
            )
    }
}
