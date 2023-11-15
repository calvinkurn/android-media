package com.tokopedia.stories.view.model

import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.utils.provider.StoriesLinkPropertiesProvider
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Unknown
import com.tokopedia.universal_sharing.view.model.LinkProperties

data class StoriesUiModel(
    val selectedGroupId: String = "",
    val selectedGroupPosition: Int = -1,
    val groupHeader: List<StoriesGroupHeader> = emptyList(),
    val groupItems: List<StoriesGroupItem> = emptyList()
)

data class StoriesGroupHeader(
    val groupId: String = "",
    val groupName: String = "",
    val image: String = "",
    val isSelected: Boolean = false
)

data class StoriesGroupItem(
    val groupId: String = "",
    val groupName: String = "",
    val detail: StoriesDetail = StoriesDetail()
)

data class StoriesDetail(
    val selectedGroupId: String = "",
    val selectedDetailPosition: Int = -1,
    val selectedDetailPositionCached: Int = -1,
    val detailItems: List<StoriesDetailItem> = emptyList()
) {
    companion object {
        val EmptyDetail get() = StoriesDetail(
            selectedGroupId = "",
            selectedDetailPosition = 0,
            selectedDetailPositionCached = 0,
            detailItems = listOf(StoriesDetailItem.Empty)
        )
    }
}

data class StoriesDetailItem(
    val id: String = "0",
    val event: StoriesDetailItemUiEvent = StoriesDetailItemUiEvent.PAUSE,
    val content: StoriesItemContent = StoriesItemContent(),
    val resetValue: Int = -1,
    val isContentLoaded: Boolean = false,
    val meta: Meta = Meta(),
    val productCount: String = "",
    val author: StoryAuthor = StoryAuthor.Unknown,
    val category: StoryCategory = StoryCategory.ASGC,
    val publishedAt: String = "",
    val menus: List<ContentMenuItem> = emptyList(),
    val share: Sharing = Sharing.Empty,
    val status: StoryStatus = StoryStatus.Unknown,
) {
    companion object {
        val Empty get() = StoriesDetailItem(event = StoriesDetailItemUiEvent.RESUME, content = StoriesItemContent(duration = 3000), resetValue = 0)
    }

    data class Meta(
        val activityTracker: String = "",
        val templateTracker: String = ""
    )

    data class StoriesItemContent(
        val type: StoriesItemContentType = Unknown,
        val data: String = "",
        val duration: Int = -1
    )

    enum class StoriesItemContentType(val value: String) {
        Image("image"), Video("video"), Unknown("0")
    }

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME, BUFFERING
    }

    data class Sharing(
        val isShareable: Boolean,
        val shareText: String,
        val metadata: LinkProperties
    ) {
        companion object {
            val Empty
                get() = Sharing(
                    isShareable = false,
                    shareText = "",
                    metadata = StoriesLinkPropertiesProvider.get(),
                )
        }
    }

    enum class StoryStatus(val value: String) {
        Active("ACTIVE"), Unknown("unknown");

        companion object {
            private val values = StoryStatus.values()

            fun getByValue(value: String): StoryStatus {
                values.forEach {
                    if (it.value.equals(value, true)) return it
                }
                return Unknown
            }
        }
    }

    enum class StoryCategory(val value: String) {
        Manual("update"), ASGC("default");

        companion object {
            fun getByValue(value: String): StoryCategory {
                values().forEach {
                    if (it.value.equals(value, true)) return it
                }
                return ASGC
            }
        }
    }

    val isProductAvailable: Boolean = productCount.isNotEmpty() && productCount != "0" && status == StoryStatus.Active
}
