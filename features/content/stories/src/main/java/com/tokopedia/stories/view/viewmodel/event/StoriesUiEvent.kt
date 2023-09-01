package com.tokopedia.stories.view.viewmodel.event

import com.tokopedia.content.common.view.ContentTaggedProductUiModel

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object OpenProduct : StoriesUiEvent

    data class Login(val onLoggedIn: () -> Unit) : StoriesUiEvent

    data class NavigateEvent(val appLink: String) : StoriesUiEvent
    data class ShowVariantSheet(val product: ContentTaggedProductUiModel) : StoriesUiEvent
    object FinishedAllStories: StoriesUiEvent
    //TODO() : add click listener if needed
    data class ShowErrorEvent(val message: Throwable) : StoriesUiEvent
    data class ShowInfoEvent(val message: Int) : StoriesUiEvent
}
