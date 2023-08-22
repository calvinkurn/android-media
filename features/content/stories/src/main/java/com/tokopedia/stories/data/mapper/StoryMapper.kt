package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoryDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoryGroupsResponseModel
import com.tokopedia.stories.view.model.StoryUiModel

interface StoryMapper {

    fun mapStoryData(
        dataGroup: StoryGroupsResponseModel,
        dataDetail: StoryDetailsResponseModel,
    ): StoryUiModel

}
