package com.tokopedia.stories.view.model

data class StoryGroupUiModel(
    val selectedPosition: Int = -1,
    val groupHeader: List<StoryGroupHeader> = emptyList(),
    val groupItems: List<StoryGroupItemUiModel> = emptyList(),
)

data class StoryGroupHeader(
    val id: String = "",
    val image: String = "",
    val title: String = "",
    val isSelected: Boolean = false,
)

data class StoryGroupItemUiModel(
    val id: String = "",
    val detail: StoryDetailUiModel = StoryDetailUiModel(),
)

data class StoryDetailUiModel(
    val selectedPosition: Int = -1,
    val selectedPositionCached: Int = -1,
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
