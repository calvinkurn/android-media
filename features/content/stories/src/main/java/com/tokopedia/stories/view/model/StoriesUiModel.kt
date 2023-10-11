package com.tokopedia.stories.view.model

import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Image
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
        val Empty get() = StoriesDetail(
            selectedGroupId = "",
            selectedDetailPosition = 0,
            selectedDetailPositionCached = 0,
            detailItems = listOf(StoriesDetailItem(event = StoriesDetailItem.StoriesDetailItemUiEvent.RESUME, content = StoriesDetailItem.StoriesItemContent(duration = 3000)))
        )
    }
}

data class StoriesDetailItem(
    val id: String = "",
    val event: StoriesDetailItemUiEvent = StoriesDetailItemUiEvent.PAUSE,
    val content: StoriesItemContent = StoriesItemContent(),
    val resetValue: Int = -1,
    val isContentLoaded: Boolean = false,
    val meta: Meta = Meta(),
    val productCount: String = "",
    val author: StoryAuthor = StoryAuthor.Unknown,
    val menus: List<ContentMenuItem> = emptyList(),
    val share: Sharing = Sharing.Empty,
    val status: StoryStatus = StoryStatus.Unknown,
) {

    data class Meta(
        val activityTracker: String = "",
        val templateTracker: String = ""
    )

    data class StoriesItemContent(
        val type: StoriesItemContentType = Image,
        val data: String = "",
        val duration: Int = -1
    )

    enum class StoriesItemContentType(val value: String) {
        Image("image"), Video("video"), Unknown("unknown")
    }

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME,
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
                    metadata = LinkProperties(),
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

    val isProductAvailable: Boolean = productCount.isNotEmpty() && productCount != "0"
}
