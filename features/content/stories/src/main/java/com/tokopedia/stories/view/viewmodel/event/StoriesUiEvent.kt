package com.tokopedia.stories.view.viewmodel.event

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int, val showAnimation: Boolean) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    data class TapSharing (val metadata: StoriesDetailItemUiModel.Sharing) : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object OpenProduct : StoriesUiEvent

    data class Login(val onLoggedIn: () -> Unit) : StoriesUiEvent

    data class NavigateEvent(val appLink: String) : StoriesUiEvent
    data class ShowVariantSheet(val product: ContentTaggedProductUiModel) : StoriesUiEvent
    object FinishedAllStories: StoriesUiEvent
    //TODO() : add click listener if needed
    data class ShowErrorEvent(val message: Throwable) : StoriesUiEvent
    data class ShowInfoEvent(val message: Int) : StoriesUiEvent
    data class ErrorGroupPage(val throwable: Throwable): StoriesUiEvent
    data class ErrorDetailPage(val throwable: Throwable): StoriesUiEvent
}
