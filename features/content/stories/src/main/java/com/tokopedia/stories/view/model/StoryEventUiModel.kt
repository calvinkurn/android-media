package com.tokopedia.stories.view.model

data class StoryUiModel(
    val selectedGroup: Int,
    val groups: List<StoryGroupUiModel>,
)

data class StoryGroupUiModel(
    val id: String,
    val image: String,
    val title: String,
    val selectedDetail: Int,
    val selected: Boolean,
    val details: List<StoryDetailUiModel>,
)

data class StoryDetailUiModel(
    val id: String,
    val selected: Int,
    val event: StoryDetailUiEvent,
    val imageContent: String,
) {

    enum class StoryDetailUiEvent {
        PAUSE, START,
    }
    companion object {
        val Empty = StoryDetailUiModel(
            id = "0",
            selected = 1,
            event = StoryDetailUiEvent.START,
            imageContent = "",
        )
    }
}
