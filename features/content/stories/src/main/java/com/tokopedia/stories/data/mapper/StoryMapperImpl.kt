package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoryDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoryGroupsResponseModel
import com.tokopedia.stories.view.model.StoryDetailItemUiModel
import com.tokopedia.stories.view.model.StoryDetailItemUiModel.StoryDetailItemUiEvent
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupItemUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel
import javax.inject.Inject

class StoryMapperImpl @Inject constructor() : StoryMapper {

    override fun mapStoryInitialData(
        dataGroup: StoryGroupsResponseModel,
        dataDetail: StoryDetailsResponseModel
    ): StoryGroupUiModel {
        return StoryGroupUiModel(
            selectedPosition = dataGroup.data.meta.selectedGroupIndex,
            groupItems = dataGroup.data.groups.mapIndexed { indexGroup, group ->
                StoryGroupItemUiModel(
                    id = group.value,
                    image = group.image,
                    title = group.name,
                    isSelected = dataGroup.data.meta.selectedGroupIndex == indexGroup,
                    detail = if (dataGroup.data.meta.selectedGroupIndex == indexGroup) {
                        StoryDetailUiModel(
                            selectedPosition = dataDetail.data.meta.selectedStoryIndex,
                            selectedPositionCached = dataDetail.data.meta.selectedStoryIndex,
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
            selectedPosition = dataDetail.data.meta.selectedStoryIndex,
            selectedPositionCached = dataDetail.data.meta.selectedStoryIndex,
            detailItems = dataDetail.data.stories.map { story ->
                StoryDetailItemUiModel(
                    id = story.id,
                    event = StoryDetailItemUiEvent.PAUSE,
                    imageContent = story.media.link,
                )
            }
        )
    }

}
