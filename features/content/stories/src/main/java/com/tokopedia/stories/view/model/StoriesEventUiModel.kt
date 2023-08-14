package com.tokopedia.stories.view.model

data class StoriesUiModel(
    val selectedGroup: Int,
    val groups: List<StoriesGroupUiModel>,
)

data class StoriesGroupUiModel(
    val id: String,
    val image: String,
    val title: String,
    val selectedDetail: Int,
    val selected: Boolean,
    val details: List<StoriesDetailUiModel>,
)

data class StoriesDetailUiModel(
    val id: String,
    val selected: Int,
    val event: StoriesDetailUiEvent,
    val imageContent: String,
) {

    enum class StoriesDetailUiEvent {
        PAUSE, RESUME, START,
    }
    companion object {
        val Empty = StoriesDetailUiModel(
            id = "0",
            selected = 1,
            event = StoriesDetailUiEvent.START,
            imageContent = "",
        )
    }
}
