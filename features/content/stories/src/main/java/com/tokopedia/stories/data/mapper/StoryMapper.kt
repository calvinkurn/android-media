package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoryDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoryGroupsResponseModel
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel

interface StoryMapper {

    fun mapInitialStoryData(
        dataGroup: StoryGroupsResponseModel,
        dataDetail: StoryDetailsResponseModel,
    ): StoryGroupUiModel

    fun mapDetailStoryRequest(
        dataDetail: StoryDetailsResponseModel,
    ): StoryDetailUiModel

}
