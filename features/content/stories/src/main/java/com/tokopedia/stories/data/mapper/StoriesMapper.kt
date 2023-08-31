package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel

interface StoriesMapper {

    fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesGroupUiModel

    fun mapStoriesDetailRequest(
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesDetailUiModel

}
