package com.tokopedia.stories.view.model

data class StoriesUiModel(
    val selectedGroup: Int,
    val groups: List<StoriesGroupUiModel>,
)

data class StoriesGroupUiModel(
    val image: String,
    val title: String,
    val selectedDetail: Int,
    val details: List<StoriesDetailUiModel>,
)

data class StoriesDetailUiModel(
    val selected: Int,
    val isPause: Boolean,
    val imageContent: String,
) {
    companion object {
        val Empty = StoriesDetailUiModel(
            selected = 1,
            isPause = false,
            imageContent = "",
        )
    }
}
