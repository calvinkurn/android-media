package com.tokopedia.stories.view.viewmodel.event


sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object FinishedAllStories: StoriesUiEvent
    //TODO() : add click listener if needed
    data class ShowErrorEvent(val message: Throwable) : StoriesUiEvent
    data class ErrorGroupPage(val throwable: Throwable): StoriesUiEvent
    data class ErrorDetailPage(val throwable: Throwable): StoriesUiEvent
}
