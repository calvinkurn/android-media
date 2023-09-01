package com.tokopedia.stories.view.model

import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.stories.uimodel.StoryAuthor

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
    val productCount: String = "",
) {

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME,
    }

}
