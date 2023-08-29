package com.tokopedia.stories.view.model

data class StoryGroupUiModel(
    val selectedPosition: Int = -1,
    val groupItems: List<StoryGroupItemUiModel> = emptyList(),
)

data class StoryGroupItemUiModel(
    val id: String = "",
    val image: String = "",
    val title: String = "",
    val isSelected: Boolean = false,
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
    val isSelected: Boolean = false,
    val resetValue: Int = -1,
) {

    enum class StoryDetailItemUiEvent {
        PAUSE, START,
    }

}
