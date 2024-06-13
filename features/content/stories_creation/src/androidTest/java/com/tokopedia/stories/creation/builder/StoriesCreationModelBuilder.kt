package com.tokopedia.stories.creation.builder

import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.stories.creation.view.model.StoriesCreationConfiguration

/**
 * Created By : Jonathan Darwin on October 24, 2023
 */
class StoriesCreationModelBuilder {

    fun buildAccountList(
        enable: Boolean = true
    ) = listOf(
        ContentAccountUiModel(
            id = "123",
            name = "Jonathan Darwin",
            iconUrl = "https://assets.tokopedia.net/assets-tokopedia-lite/v2/arael/kratos/36c1015e.png",
            type = "content-shop",
            hasUsername = true,
            hasAcceptTnc = true,
            enable = enable,
        )
    )

    fun buildStoriesInfo(
        storiesId: String = "123",
        storiesApplinkTemplate: String = "asdf",
    ) = StoriesCreationConfiguration(
        storiesId = storiesId,
        maxProductTag = 30,
        storyDuration = "24 Jam",
        minVideoDuration = 1,
        maxVideoDuration = 2,
        imageSourceId = "asdf",
        videoSourceId = "asdf",
        maxStoriesConfig = StoriesCreationConfiguration.MaxStoriesConfig.Empty,
        storiesApplinkTemplate = storiesApplinkTemplate,
    )
}
