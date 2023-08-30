package com.tokopedia.stories.view.model

data class StoryGroupUiModel(
    val selectedGroupId: String = "",
    val selectedGroupPosition: Int = -1,
    val groupHeader: List<StoryGroupHeader> = emptyList(),
    val groupItems: List<StoryGroupItemUiModel> = emptyList(),
)

data class StoryGroupHeader(
    val groupId: String = "",
    val image: String = "",
    val title: String = "",
    val isSelected: Boolean = false,
)

data class StoryGroupItemUiModel(
    val groupId: String = "",
    val detail: StoryDetailUiModel = StoryDetailUiModel(),
)

data class StoryDetailUiModel(
    val selectedGroupId: String = "",
    val selectedDetailPosition: Int = -1,
    val selectedDetailPositionCached: Int = -1,
    val detailItems: List<StoryDetailItemUiModel> = emptyList(),
)

data class StoryDetailItemUiModel(
    val id: String = "",
    val event: StoryDetailItemUiEvent = StoryDetailItemUiEvent.PAUSE,
    val imageContent: String = "",
    val resetValue: Int = -1,
) {

    enum class StoryDetailItemUiEvent {
        PAUSE, RESUME,
    }

}
