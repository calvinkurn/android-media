package com.tokopedia.stories.model

import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel

fun mockInitialDataModel(
    selectedGroup: Int = 0,
    selectedDetail: Int = 0,
    isCached: Boolean = true,
): StoriesUiModel {
    return StoriesUiModel(
        selectedGroupId = if (isCached) "groupId $selectedGroup" else "",
        selectedGroupPosition = selectedGroup,
        groupHeader = listOf(
            StoriesGroupHeader(
                groupId = "groupId 0",
                groupName = "Group Name 0",
                image = "Image Group 0",
                isSelected = 0 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 1",
                groupName = "Group Name 1",
                image = "Image Group 1",
                isSelected = 1 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 3",
                groupName = "Group Name 3",
                image = "Image Group 3",
                isSelected = 2 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 4",
                groupName = "Group Name 4",
                image = "Image Group 4",
                isSelected = 3 == selectedGroup,
            ),
        ),
        groupItems = listOf(
            StoriesGroupItem(
                groupId = if (isCached) "groupId 0" else "",
                groupName = "Group Name 0",
                detail = if (isCached) {
                    StoriesDetail(
                        selectedGroupId = "groupId $selectedGroup",
                        selectedDetailPosition = selectedDetail,
                        selectedDetailPositionCached = selectedDetail,
                        detailItems = listOf(
                            StoriesDetailItem(
                                id = "0",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 0",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 1",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 2",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                        ),
                    )
                } else StoriesDetail()
            ),
            StoriesGroupItem(
                groupId = if (isCached) "groupId 1" else "",
                groupName = "Group Name 1",
                detail = if (isCached) {
                    StoriesDetail(
                        selectedGroupId = "groupId $selectedGroup",
                        selectedDetailPosition = selectedDetail,
                        selectedDetailPositionCached = selectedDetail,
                        detailItems = listOf(
                            StoriesDetailItem(
                                id = "0",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 0",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 1",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 2",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                        ),
                    )
                } else StoriesDetail(),
            ),
            StoriesGroupItem(
                groupId = if (isCached) "groupId 2" else "",
                groupName = "Group Name 2",
                detail = if (isCached) {
                    StoriesDetail(
                        selectedGroupId = "groupId $selectedGroup",
                        selectedDetailPosition = selectedDetail,
                        selectedDetailPositionCached = selectedDetail,
                        detailItems = listOf(
                            StoriesDetailItem(
                                id = "0",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 0",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 1",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                    data = "data 2",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isSameContent = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                            ),
                        ),
                    )
                } else StoriesDetail(),
            ),
        ),
    )
}

fun mockInitialDataModelFetchPrevAndNext(): StoriesUiModel {
    return StoriesUiModel(
        selectedGroupId = "groupId 1",
        selectedGroupPosition = 1,
        groupHeader = listOf(
            StoriesGroupHeader(
                groupId = "groupId 0",
                groupName = "Group Name 0",
                image = "Image Group 0",
                isSelected = false,
            ),
            StoriesGroupHeader(
                groupId = "groupId 1",
                groupName = "Group Name 1",
                image = "Image Group 1",
                isSelected = true,
            ),
            StoriesGroupHeader(
                groupId = "groupId 3",
                groupName = "Group Name 3",
                image = "Image Group 3",
                isSelected = false,
            ),
            StoriesGroupHeader(
                groupId = "groupId 4",
                groupName = "Group Name 4",
                image = "Image Group 4",
                isSelected = false,
            ),
        ),
        groupItems = listOf(
            StoriesGroupItem(groupId = "groupId 0"),
            StoriesGroupItem(
                groupId = "groupId 1",
                groupName = "Group Name 1",
                detail = StoriesDetail(
                    selectedGroupId = "groupId 1",
                    selectedDetailPosition = 0,
                    selectedDetailPositionCached = 0,
                    detailItems = listOf(
                        StoriesDetailItem(
                            id = "0",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 0",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "1",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 1",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "2",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 2",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                    ),
                ),
            ),
            StoriesGroupItem(groupId = "groupId 2"),
        ),
    )
}

fun mockInitialDataModelFetchPrev(): StoriesUiModel {
    return StoriesUiModel(
        selectedGroupId = "groupId 2",
        selectedGroupPosition = 2,
        groupHeader = listOf(
            StoriesGroupHeader(
                groupId = "groupId 0",
                groupName = "Group Name 0",
                image = "Image Group 0",
                isSelected = false,
            ),
            StoriesGroupHeader(
                groupId = "groupId 1",
                groupName = "Group Name 1",
                image = "Image Group 1",
                isSelected = true,
            ),
            StoriesGroupHeader(
                groupId = "groupId 3",
                groupName = "Group Name 3",
                image = "Image Group 3",
                isSelected = false,
            ),
            StoriesGroupHeader(
                groupId = "groupId 4",
                groupName = "Group Name 4",
                image = "Image Group 4",
                isSelected = false,
            ),
        ),
        groupItems = listOf(
            StoriesGroupItem(groupId = "groupId 0"),
            StoriesGroupItem(groupId = "groupId 1"),
            StoriesGroupItem(
                groupId = "groupId 2",
                groupName = "Group Name 2",
                detail = StoriesDetail(
                    selectedGroupId = "groupId 2",
                    selectedDetailPosition = 0,
                    selectedDetailPositionCached = 0,
                    detailItems = listOf(
                        StoriesDetailItem(
                            id = "0",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 0",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "1",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 1",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "2",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 2",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                    ),
                ),
            ),
        ),
    )
}

fun mockInitialDataModelFetchNext(): StoriesUiModel {
    return StoriesUiModel(
        selectedGroupId = "groupId 0",
        selectedGroupPosition = 0,
        groupHeader = listOf(
            StoriesGroupHeader(
                groupId = "groupId 0",
                groupName = "Group Name 0",
                image = "Image Group 0",
                isSelected = false,
            ),
            StoriesGroupHeader(
                groupId = "groupId 1",
                groupName = "Group Name 1",
                image = "Image Group 1",
                isSelected = true,
            ),
            StoriesGroupHeader(
                groupId = "groupId 3",
                groupName = "Group Name 3",
                image = "Image Group 3",
                isSelected = false,
            ),
            StoriesGroupHeader(
                groupId = "groupId 4",
                groupName = "Group Name 4",
                image = "Image Group 4",
                isSelected = false,
            ),
        ),
        groupItems = listOf(
            StoriesGroupItem(
                groupId = "groupId 0",
                groupName = "Group Name 0",
                detail = StoriesDetail(
                    selectedGroupId = "groupId 0",
                    selectedDetailPosition = 0,
                    selectedDetailPositionCached = 0,
                    detailItems = listOf(
                        StoriesDetailItem(
                            id = "0",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 0",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "1",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 1",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "2",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.IMAGE,
                                data = "data 2",
                                duration = 7 * 1000,
                            ),
                            resetValue = -1,
                            isSameContent = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                    ),
                ),
            ),
            StoriesGroupItem(groupId = "groupId 1"),
            StoriesGroupItem(groupId = "groupId 2"),
        ),
    )
}

fun StoriesUiModel.mockMainDataResetValue(expectedData: StoriesUiModel): StoriesUiModel {
    return this.copy(groupItems = this.groupItems.mapIndexed { indexGroupItems, storiesGroupItem ->
        storiesGroupItem.copy(
            detail = storiesGroupItem.detail.copy(
                detailItems = storiesGroupItem.detail.detailItems.mapIndexed { indexDetailItems, storiesDetailItem ->
                    storiesDetailItem.copy(resetValue = expectedData.groupItems[indexGroupItems].detail.detailItems[indexDetailItems].resetValue)
                }
            )
        )
    }
    )
}

fun StoriesGroupItem.mockGroupResetValue(expectedGroupDetail: List<StoriesDetailItem>): StoriesGroupItem {
    return this.copy(
        detail = this.detail.copy(
            detailItems = this.detail.detailItems.mapIndexed { index, storiesDetailItem ->
                storiesDetailItem.copy(resetValue = expectedGroupDetail[index].resetValue)
            }
        )
    )
}

fun StoriesDetailItem.mockDetailResetValue(expectedDetail: StoriesDetailItem): StoriesDetailItem {
    return this.copy(resetValue = expectedDetail.resetValue)
}
