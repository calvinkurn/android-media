package com.tokopedia.stories.view.viewmodel.event

sealed interface StoriesUiEvent {
    data class SelectCategories(val position: Int) : StoriesUiEvent
}
