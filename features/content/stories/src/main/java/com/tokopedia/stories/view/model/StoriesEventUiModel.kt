package com.tokopedia.stories.view.model

data class StoriesUiModel(
    val categories: List<StoriesCategoriesUiModel>,
    val stories: List<StoriesDataUiModel>,
) {
    companion object {
        val Empty = StoriesUiModel(emptyList(), emptyList())
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

data class StoriesCategoriesUiModel(
    val image: String,
    val title: String,
) {
    companion object {
        val Empty = StoriesCategoriesUiModel(
            image = "",
            title = "",
        )
    }
}

