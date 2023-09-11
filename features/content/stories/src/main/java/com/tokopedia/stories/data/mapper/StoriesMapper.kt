package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesUiModel

interface StoriesMapper {

    fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesUiModel

    fun mapStoriesDetailRequest(
        selectedGroupId: String,
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesDetailUiModel

}
