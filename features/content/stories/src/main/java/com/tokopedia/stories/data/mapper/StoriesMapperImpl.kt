package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesItemContent
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesItemContentType.IMAGE
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesItemContentType.VIDEO
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItemUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import javax.inject.Inject

class StoriesMapperImpl @Inject constructor() : StoriesMapper {

    override fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel
    ): StoriesUiModel {
        return StoriesUiModel(
            selectedGroupId = dataGroup.data.groups[dataGroup.data.meta.selectedGroupIndex].value,
            selectedGroupPosition = dataGroup.data.meta.selectedGroupIndex,
            groupHeader = dataGroup.data.groups.mapIndexed { indexGroupHeader, group ->
                StoriesGroupHeader(
                    groupId = group.value,
                    image = group.image,
                    title = group.name,
                    isSelected = dataGroup.data.meta.selectedGroupIndex == indexGroupHeader,
                )
            },
            groupItems = dataGroup.data.groups.mapIndexed { indexGroupItem, group ->
                StoriesGroupItemUiModel(
                    groupId = group.value,
                    groupName = group.name,
                    detail = if (dataGroup.data.meta.selectedGroupIndex == indexGroupItem) {
                        StoriesDetailUiModel(
                            selectedGroupId = group.value,
                            selectedDetailPosition = dataDetail.data.meta.selectedStoriesIndex,
                            selectedDetailPositionCached = dataDetail.data.meta.selectedStoriesIndex,
                            detailItems = dataDetail.data.stories.map { stories ->
                                StoriesDetailItemUiModel(
                                    id = stories.id,
                                    event = StoriesDetailItemUiEvent.PAUSE,
                                    content = StoriesItemContent(
                                        type = if (stories.media.type == IMAGE.value) IMAGE else VIDEO,
                                        data = stories.media.link,
                                        duration = 7 * 1000,
                                    ),
                                    resetValue = -1,
                                    isSameContent = false,
                                )
                            }
                        )
                    } else StoriesDetailUiModel()
                )
            }
        )
    }

    override fun mapStoriesDetailRequest(dataDetail: StoriesDetailsResponseModel): StoriesDetailUiModel {
        return StoriesDetailUiModel(
            selectedGroupId = "",
            selectedDetailPosition = dataDetail.data.meta.selectedStoriesIndex,
            selectedDetailPositionCached = dataDetail.data.meta.selectedStoriesIndex,
            detailItems = dataDetail.data.stories.map { stories ->
                StoriesDetailItemUiModel(
                    id = stories.id,
                    event = StoriesDetailItemUiEvent.PAUSE,
                    content = StoriesItemContent(
                        type = if (stories.media.type == IMAGE.value) IMAGE else VIDEO,
                        data = stories.media.link,
                        duration = 7 * 1000,
                    ),
                    resetValue = -1,
                    isSameContent = false,
                )
            }
        )
    }

}
