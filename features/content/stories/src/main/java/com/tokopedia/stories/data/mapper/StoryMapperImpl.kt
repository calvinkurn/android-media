package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoryDetailsResponseModel
import com.tokopedia.stories.domain.model.group.StoryGroupsResponseModel
import com.tokopedia.stories.view.model.StoryUiModel
import com.tokopedia.stories.view.model.StoryUiModel.StoryDetailUiModel.StoryDetailUiEvent
import com.tokopedia.stories.view.model.StoryUiModel.StoryGroupUiModel
import javax.inject.Inject

class StoryMapperImpl @Inject constructor()  : StoryMapper {

    override fun mapStoryData(
        dataGroup: StoryGroupsResponseModel,
        dataDetail: StoryDetailsResponseModel
    ): StoryUiModel {
        return StoryUiModel(
            selectedGroup = dataGroup.data.meta.selectedGroupIndex,
            groups = dataGroup.data.groups.mapIndexed { index, group ->
                StoryGroupUiModel(
                    id = group.value,
                    image = group.image,
                    selectedDetail = dataDetail.data.meta.selectedStoryIndex,
                    title = group.name,
                    selected = dataGroup.data.meta.selectedGroupIndex == index,
                    details = dataDetail.data.stories.mapIndexed { index, detail ->
                        StoryUiModel.StoryDetailUiModel(
                            id = detail.id,
                            selected = dataDetail.data.meta.selectedStoryIndex,
                            event = StoryDetailUiEvent.PAUSE,
                            imageContent = detail.media.link,
                        )
                    }
                )
            }
        )
    }

}
