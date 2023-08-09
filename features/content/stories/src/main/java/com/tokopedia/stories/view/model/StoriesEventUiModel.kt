package com.tokopedia.stories.view.model

data class StoriesUiModel(
    val stories: List<StoriesDataUiModel>,
) {
    companion object {
        val Empty = StoriesUiModel(emptyList())
    }
}

data class StoriesDataUiModel(
    val count: Int,
    val selected: Int,
    val isPause: Boolean,
) {

    companion object {
        val Empty = StoriesDataUiModel(
            count = 0,
            selected = 0,
            isPause = false,
        )
    }
}

