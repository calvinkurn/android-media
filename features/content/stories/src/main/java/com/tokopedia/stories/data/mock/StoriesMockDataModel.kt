package com.tokopedia.stories.data.mock

import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.UserReportOptions
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.uimodel.StoryAuthor
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
    isProductCountEmpty: Boolean = false,
    productCount: Int = 0,
    duration: Int = 1000,
    storiesCategory: StoriesDetailItem.StoryCategory = StoriesDetailItem.StoryCategory.ASGC,
): StoriesUiModel {
    return StoriesUiModel(
        selectedGroupId = if (isCached) "groupId $selectedGroup" else "",
        selectedGroupPosition = selectedGroup,
        groupHeader = listOf(
            StoriesGroupHeader(
                groupId = "groupId 0",
                groupName = "Group Name 0",
                image = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                isSelected = 0 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 1",
                groupName = "Group Name 1",
                image = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                isSelected = 1 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 2",
                groupName = "Group Name 2",
                image = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                isSelected = 2 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 3",
                groupName = "Group Name 3",
                image = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                isSelected = 3 == selectedGroup,
            ),
            StoriesGroupHeader(
                groupId = "groupId 4",
                groupName = "Group Name 4",
                image = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                isSelected = 4 == selectedGroup,
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
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
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
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
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
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                        ),
                    )
                } else StoriesDetail(),
            ),
            StoriesGroupItem(
                groupId = if (isCached) "groupId 3" else "",
                groupName = "Group Name 3",
                detail = if (isCached) {
                    StoriesDetail(
                        selectedGroupId = "groupId $selectedGroup",
                        selectedDetailPosition = selectedDetail,
                        selectedDetailPositionCached = selectedDetail,
                        detailItems = listOf(
                            StoriesDetailItem(
                                id = "0",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                        ),
                    )
                } else StoriesDetail(),
            ),
            StoriesGroupItem(
                groupId = if (isCached) "groupId 4" else "",
                groupName = "Group Name 4",
                detail = if (isCached) {
                    StoriesDetail(
                        selectedGroupId = "groupId $selectedGroup",
                        selectedDetailPosition = selectedDetail,
                        selectedDetailPositionCached = selectedDetail,
                        detailItems = listOf(
                            StoriesDetailItem(
                                id = "0",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "1",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                            StoriesDetailItem(
                                id = "2",
                                event = StoriesDetailItemUiEvent.PAUSE,
                                category = storiesCategory,
                                content = StoriesDetailItem.StoriesItemContent(
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                    duration = duration
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                author = StoryAuthor.Shop(
                                    shopName = "Joe Shop",
                                    shopId = "123",
                                    avatarUrl = "",
                                    badgeUrl = "",
                                    appLink = "",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
                            ),
                        ),
                    )
                } else StoriesDetail(),
            ),
        ),
    )
}


fun mockInitialDataModelForDeleteStories(
    selectedGroup: Int = 0,
    selectedDetail: Int = 0,
    isCached: Boolean = true,
    isProductCountEmpty: Boolean = false,
    productCount: Int = 0,
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
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "data 0",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
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
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "data 0",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
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
                                    type = StoriesDetailItem.StoriesItemContentType.Image,
                                    data = "data 0",
                                    duration = 7 * 1000,
                                ),
                                resetValue = -1,
                                isContentLoaded = false,
                                meta = StoriesDetailItem.Meta(
                                    activityTracker = "12345",
                                    templateTracker = "1235df8",
                                ),
                                productCount = if (isProductCountEmpty) "" else "$productCount",
                                status = StoriesDetailItem.StoryStatus.Active,
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
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                            status = StoriesDetailItem.StoryStatus.Active,
                        ),
                        StoriesDetailItem(
                            id = "1",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                            status = StoriesDetailItem.StoryStatus.Active,
                        ),
                        StoriesDetailItem(
                            id = "2",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                            status = StoriesDetailItem.StoryStatus.Active,
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
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                            status = StoriesDetailItem.StoryStatus.Active,
                        ),
                        StoriesDetailItem(
                            id = "1",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                            status = StoriesDetailItem.StoryStatus.Active,
                        ),
                        StoriesDetailItem(
                            id = "2",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                            status = StoriesDetailItem.StoryStatus.Active,
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
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/784aad6f-728a-43b4-9050-350aed7d79c6.jpg?b=UFN%25%24%3FMxq%5DyD%5BntlS6ng4Ux%5D%251Rj9%25ROt7%25g",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "1",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/cache/296/jJtrdn/2023/8/26/0f1c281b-260c-47d3-9e2d-5b6dda318d7c.png?b=UCAWdEfhL8j%5D%23tfPO7a%23y%2Bf8QsfOpqa%23Znoc",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
                            meta = StoriesDetailItem.Meta(
                                activityTracker = "12345",
                                templateTracker = "1235df8",
                            ),
                        ),
                        StoriesDetailItem(
                            id = "2",
                            event = StoriesDetailItemUiEvent.PAUSE,
                            content = StoriesDetailItem.StoriesItemContent(
                                type = StoriesDetailItem.StoriesItemContentType.Image,
                                data = "https://images.tokopedia.net/img/generator/LScDrk/a1198bf4080d320f6563d7e1896f1abd.jpg",
                                duration = 1000
                            ),
                            resetValue = -1,
                            isContentLoaded = false,
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

fun mockContentTaggedProductUiModel(): ContentTaggedProductUiModel {
    return ContentTaggedProductUiModel(
        id = "",
        parentID = "",
        showGlobalVariant = false,
        shop = ContentTaggedProductUiModel.Shop(id = "", name = ""),
        title = "",
        imageUrl = "",
        price = ContentTaggedProductUiModel.NormalPrice(formattedPrice = "", price = 0.0),
        appLink = "",
        campaign = ContentTaggedProductUiModel.Campaign(
            type = ContentTaggedProductUiModel.CampaignType.NoCampaign,
            status = ContentTaggedProductUiModel.CampaignStatus.Upcoming,
            isExclusiveForMember = false
        ),
        affiliate = ContentTaggedProductUiModel.Affiliate(id = "", channel = ""),
        stock = ContentTaggedProductUiModel.Stock.OutOfStock,
    )
}

fun mockReportReasonList(): List<PlayUserReportReasoningUiModel.Reasoning> = listOf(
    PlayUserReportReasoningUiModel.Reasoning(
        reasoningId = 1,
        title = "Reason One",
        detail = "Report Reason",
        submissionData = UserReportOptions.OptionAdditionalField(),
    ),
    PlayUserReportReasoningUiModel.Reasoning(
        reasoningId = 2,
        title = "Reason Two",
        detail = "Report Reason",
        submissionData = UserReportOptions.OptionAdditionalField(),
    ),
)
