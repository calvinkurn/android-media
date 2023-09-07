package com.tokopedia.stories.view.viewmodel.event

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int, val showAnimation: Boolean) : StoriesUiEvent
    data class ErrorGroupPage(val throwable: Throwable): StoriesUiEvent
    data class ErrorDetailPage(val throwable: Throwable): StoriesUiEvent

    object FinishedAllStories: StoriesUiEvent
}
