package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoryDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoryGroupsResponseModel
import com.tokopedia.stories.view.model.StoryDetailItemUiModel
import com.tokopedia.stories.view.model.StoryDetailItemUiModel.StoryDetailItemUiEvent
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupHeader
import com.tokopedia.stories.view.model.StoryGroupItemUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel
import javax.inject.Inject

class StoryMapperImpl @Inject constructor() : StoryMapper {

    override fun mapStoryInitialData(
        dataGroup: StoryGroupsResponseModel,
        dataDetail: StoryDetailsResponseModel
    ): StoryGroupUiModel {
        return StoryGroupUiModel(
            selectedGroupId = dataGroup.data.groups[dataGroup.data.meta.selectedGroupIndex].value,
            selectedGroupPosition = dataGroup.data.meta.selectedGroupIndex,
            groupHeader = dataGroup.data.groups.mapIndexed { indexGroupHeader, group ->
                StoryGroupHeader(
                    groupId = group.value,
                    image = group.image,
                    title = group.name,
                    isSelected = dataGroup.data.meta.selectedGroupIndex == indexGroupHeader,
                )
            },
            groupItems = dataGroup.data.groups.mapIndexed { indexGroupItem, group ->
                StoryGroupItemUiModel(
                    groupId = group.value,
                    detail = if (dataGroup.data.meta.selectedGroupIndex == indexGroupItem) {
                        StoryDetailUiModel(
                            selectedGroupId = group.value,
                            selectedDetailPosition = dataDetail.data.meta.selectedStoryIndex,
                            selectedDetailPositionCached = dataDetail.data.meta.selectedStoryIndex,
                            detailItems = dataDetail.data.stories.map { story ->
                                StoryDetailItemUiModel(
                                    id = story.id,
                                    event = StoryDetailItemUiEvent.PAUSE,
                                    imageContent = story.media.link,
                                )
                            }
                        )
                    } else StoryDetailUiModel()
                )
            }
        )
    }

    override fun mapStoryDetailRequest(dataDetail: StoryDetailsResponseModel): StoryDetailUiModel {
        return StoryDetailUiModel(
            selectedDetailPosition = dataDetail.data.meta.selectedStoryIndex,
            selectedDetailPositionCached = dataDetail.data.meta.selectedStoryIndex,
            detailItems = dataDetail.data.stories.map { story ->
                StoryDetailItemUiModel(
                    id = story.id,
                    event = StoryDetailItemUiEvent.PAUSE,
                    imageContent = story.media.link,
                    resetValue = -1,
                )
            }
        )
    }

}
