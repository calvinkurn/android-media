package com.tokopedia.stories.view.viewmodel.event

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object OpenProduct : StoriesUiEvent

    data class Login(val onLoggedIn: () -> Unit) : StoriesUiEvent

    data class NavigateEvent(val appLink: String) : StoriesUiEvent
}
