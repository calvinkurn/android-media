package com.tokopedia.stories.creation.builder

import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on October 18, 2023
 */
class ConfigurationModelBuilder {

    fun build(
        storiesId: String = "asdf",
        maxProductTag: Int = 30,
        storyDuration: String = "24 Jam",
        minVideoDuration: Int = 1,
        maxVideoDuration: Int = 90,
        imageSourceId: String = "asdf",
        videoSourceId: String = "asdf",
        maxStoriesConfig: StoriesCreationConfiguration.MaxStoriesConfig = StoriesCreationConfiguration.MaxStoriesConfig.Empty,
        storiesApplinkTemplate: String = "tokopedia://stories/shop/123"
    ) = StoriesCreationConfiguration(
        storiesId = storiesId,
        maxProductTag = maxProductTag,
        storyDuration = storyDuration,
        minVideoDuration = minVideoDuration,
        maxVideoDuration = maxVideoDuration,
        imageSourceId = imageSourceId,
        videoSourceId = videoSourceId,
        maxStoriesConfig = maxStoriesConfig,
        storiesApplinkTemplate = storiesApplinkTemplate,
    )
}
