package com.tokopedia.stories.view.model

data class StoriesUiModel(
    val group: List<StoriesGroupUiModel>,
)
data class StoriesGroupUiModel(
    val image: String,
    val title: String,
    val selectedStories: Int,
    val stories: List<StoriesDetailUiModel>,
)

data class StoriesDetailUiModel(
    val position: Int,
    val selected: Boolean,
    val isPause: Boolean,
    val imageContent: String,
)
