package com.tokopedia.stories.data.mapper

import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel
import com.tokopedia.stories.domain.model.detail.StoriesDetailsResponseModel.ContentStoriesDetails
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel
import com.tokopedia.stories.domain.model.group.StoriesGroupsResponseModel.ContentStoriesGroups
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.Meta
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.IMAGE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.VIDEO
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel
import javax.inject.Inject

class StoriesMapperImpl @Inject constructor() : StoriesMapper {

    override fun mapStoriesInitialData(
        dataGroup: StoriesGroupsResponseModel,
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesUiModel {
        val groupsData = dataGroup.data
        if (groupsData == ContentStoriesGroups()) return StoriesUiModel()

        val groupSelectedPos = dataGroup.data.meta.selectedGroupIndex
        val groupsItem = groupsData.groups
        return StoriesUiModel(
            selectedGroupId = groupsItem[groupSelectedPos].value,
            selectedGroupPosition = groupSelectedPos,
            groupHeader = groupsItem.mapIndexed { indexGroupHeader, group ->
                StoriesGroupHeader(
                    groupId = group.value,
                    image = group.image,
                    groupName = group.name,
                    isSelected = groupSelectedPos == indexGroupHeader,
                )
            },
            groupItems = groupsItem.mapIndexed { indexGroupItem, group ->
                StoriesGroupItem(
                    groupId = group.value,
                    groupName = group.name,
                    detail = if (groupSelectedPos == indexGroupItem) {
                        mapStoriesDetailRequest(
                            selectedGroupId = group.value,
                            dataDetail = dataDetail,
                        )
                    } else StoriesDetail()
                )
            }
        )
    }

    override fun mapStoriesDetailRequest(
        selectedGroupId: String,
        dataDetail: StoriesDetailsResponseModel,
    ): StoriesDetail {
        val detailData = dataDetail.data
        if (detailData == ContentStoriesDetails()) return StoriesDetail()

        val storiesSelectedPos = detailData.meta.selectedStoriesIndex
        val storiesItem = detailData.stories
        return StoriesDetail(
            selectedGroupId = selectedGroupId,
            selectedDetailPosition = storiesSelectedPos,
            selectedDetailPositionCached = storiesSelectedPos,
            detailItems = storiesItem.map { stories ->
                StoriesDetailItem(
                    id = stories.id,
                    event = StoriesDetailItemUiEvent.PAUSE,
                    content = StoriesItemContent(
                        type = if (stories.media.type == IMAGE.value) IMAGE else VIDEO,
                        data = stories.media.link,
                        duration = 7 * 1000,
                    ),
                    resetValue = -1,
                    isSameContent = false,
                    meta = Meta(
                        activityTracker = stories.meta.activityTracker,
                        templateTracker = stories.meta.templateTracker,
                    ),
                )
            }
        )
    }

}
