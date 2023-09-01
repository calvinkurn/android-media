package com.tokopedia.stories.view.viewmodel.event

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object FinishedAllStories: StoriesUiEvent
    data class ErrorGroupPage(val throwable: Throwable): StoriesUiEvent
    data class ErrorDetailPage(val throwable: Throwable): StoriesUiEvent
}
