package com.tokopedia.stories.creation.builder

import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class ConfigurationModelBuilder {

    fun build(
        storiesId: String = "asdf",
        maxProductTag: Int = 30,
        showDuration: String = "24 Jam",
        minVideoDuration: Long = 1L,
        maxVideoDuration: Long = 90L,
        imageSourceId: String = "asdf",
        videoSourceId: String = "asdf",
        maxStoriesConfig: StoriesCreationConfiguration.MaxStoriesConfig = StoriesCreationConfiguration.MaxStoriesConfig.Empty,
    ) = StoriesCreationConfiguration(
        storiesId = storiesId,
        maxProductTag = maxProductTag,
        showDuration = showDuration,
        minVideoDuration = minVideoDuration,
        maxVideoDuration = maxVideoDuration,
        imageSourceId = imageSourceId,
        videoSourceId = videoSourceId,
        maxStoriesConfig = maxStoriesConfig
    )
}
