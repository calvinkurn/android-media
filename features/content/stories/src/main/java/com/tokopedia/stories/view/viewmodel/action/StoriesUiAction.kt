package com.tokopedia.stories.view.viewmodel.action

import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType

sealed interface StoriesUiAction {
    data class SetMainData(val selectedGroup: Int) : StoriesUiAction
    data class SelectGroup(val selectedGroup: Int, val showAnimation: Boolean) : StoriesUiAction
    data class CollectImpressedGroup(val data: StoriesGroupHeader) : StoriesUiAction
    data class DismissSheet(val type: BottomSheetType) : StoriesUiAction
    data class Navigate(val appLink: String) : StoriesUiAction
    data class ShowVariantSheet(val product: ContentTaggedProductUiModel) : StoriesUiAction
    data class ProductAction(
        val action: StoriesProductAction,
        val product: ContentTaggedProductUiModel
    ) : StoriesUiAction

    object PageIsSelected : StoriesUiAction
    object NextDetail : StoriesUiAction
    object PreviousDetail : StoriesUiAction
    object PauseStories : StoriesUiAction
    object ResumeStories : StoriesUiAction
    object OpenKebabMenu : StoriesUiAction
    object TapSharing : StoriesUiAction
    object ShowDeleteDialog : StoriesUiAction
    object OpenProduct : StoriesUiAction
    object FetchProduct : StoriesUiAction
    object DeleteStory : StoriesUiAction
    object ContentIsLoaded : StoriesUiAction
    object SetInitialData : StoriesUiAction
    object RetryDetailPage : StoriesUiAction
}

enum class StoriesProductAction {
    Atc, Buy;
}
