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
    val isPause: Boolean,
    val imageContent: String,
) {
    companion object {
        val Empty = StoriesDetailUiModel(
            id = "0",
            selected = 1,
            isPause = false,
            imageContent = "",
        )
    }
}
