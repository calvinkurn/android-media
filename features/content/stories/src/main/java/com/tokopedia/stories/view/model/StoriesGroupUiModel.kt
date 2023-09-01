package com.tokopedia.stories.view.model

import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.universal_sharing.view.model.LinkProperties

data class StoriesGroupUiModel(
    val selectedGroupId: String = "",
    val selectedGroupPosition: Int = -1,
    val groupHeader: List<StoriesGroupHeader> = emptyList(),
    val groupItems: List<StoriesGroupItemUiModel> = emptyList(),
)

data class StoriesGroupHeader(
    val groupId: String = "",
    val image: String = "",
    val title: String = "",
    val isSelected: Boolean = false,
)

data class StoriesGroupItemUiModel(
    val groupId: String = "",
    val detail: StoriesDetailUiModel = StoriesDetailUiModel(),
)

data class StoriesDetailUiModel(
    val selectedGroupId: String = "",
    val selectedDetailPosition: Int = -1,
    val selectedDetailPositionCached: Int = -1,
    val detailItems: List<StoriesDetailItemUiModel> = emptyList(),
)

data class StoriesDetailItemUiModel(
    val id: String = "",
    val event: StoriesDetailItemUiEvent = StoriesDetailItemUiEvent.PAUSE,
    val imageContent: String = "",
    val resetValue: Int = -1,
    val isSameContent: Boolean = false,
    val author: StoryAuthor = StoryAuthor.Unknown,
    val menus: List<ContentMenuItem> = emptyList(),
    val share: Sharing,
    val productCount: String = "",
) {

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME,
    }

    data class Sharing(
        val isShareable: Boolean,
        val metadata: LinkProperties,
    ) {
        companion object {
            val Empty get() = Sharing(isShareable = false, metadata = LinkProperties())
        }
    }
}
