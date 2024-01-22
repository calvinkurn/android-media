package com.tokopedia.stories.view.viewmodel.event

import androidx.annotation.StringRes
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.viewmodel.action.StoriesProductAction

sealed interface StoriesUiEvent {
    data class SelectGroup(val position: Int, val showAnimation: Boolean) : StoriesUiEvent
    object OpenKebab : StoriesUiEvent
    data class TapSharing(val metadata: StoriesDetailItem.Sharing) : StoriesUiEvent
    object ShowDeleteDialog : StoriesUiEvent
    object OpenProduct : StoriesUiEvent

    data class Login(val onLoggedIn: () -> Unit) : StoriesUiEvent

    data class NavigateEvent(val appLink: String) : StoriesUiEvent
    data class ShowVariantSheet(val product: ContentTaggedProductUiModel) : StoriesUiEvent
    data class ShowErrorEvent(val message: Throwable) : StoriesUiEvent
    data class ShowInfoEvent(@StringRes val message: Int) : StoriesUiEvent
    data class ErrorGroupPage(val throwable: Throwable, val onClick: () -> Unit) : StoriesUiEvent
    data class ErrorDetailPage(val throwable: Throwable, val onClick: () -> Unit) : StoriesUiEvent

    data class ErrorFetchCaching(val throwable: Throwable) : StoriesUiEvent
    data class ErrorSetTracking(val throwable: Throwable) : StoriesUiEvent

    object EmptyGroupPage : StoriesUiEvent
    object EmptyDetailPage : StoriesUiEvent
    object FinishedAllStories : StoriesUiEvent
    data class ProductSuccessEvent(val action: StoriesProductAction, @StringRes val message: Int) :
        StoriesUiEvent

    object ShowStoriesTimeCoachmark : StoriesUiEvent
    object OnboardShown : StoriesUiEvent
}
