package com.tokopedia.stories.view.viewmodel.event

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object FinishedAllStories: StoriesUiEvent
}
