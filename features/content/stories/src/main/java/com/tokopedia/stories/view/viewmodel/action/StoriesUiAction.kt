package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.StoriesGroupHeader
import com.tokopedia.stories.view.viewmodel.state.BottomSheetType

sealed interface StoriesUiAction {
    data class SetMainData(val selectedGroup: Int) : StoriesUiAction
    data class SelectGroup(val selectedGroup: Int, val showAnimation: Boolean) : StoriesUiAction
    data class CollectImpressedGroup(val data: StoriesGroupHeader) : StoriesUiAction

    data class SetArgumentsData(val data: Bundle?) : StoriesUiAction
    data class SetGroupMainData(val selectedGroup: Int) : StoriesUiAction

    object NextDetail : StoriesUiAction
    data class SetGroup(val selectedGroup: Int, val showAnimation: Boolean): StoriesUiAction

    object PreviousDetail : StoriesUiAction

    object PauseStories : StoriesUiAction

    object ResumeStories : StoriesUiAction

    object OpenKebabMenu : StoriesUiAction

    data class DismissSheet(val type: BottomSheetType) : StoriesUiAction

    object ShowDeleteDialog : StoriesUiAction

    object OpenProduct : StoriesUiAction

    data class ProductAction(
        val action: StoriesProductAction,
        val product: ContentTaggedProductUiModel
    ) : StoriesUiAction

    data class Navigate(val appLink: String) : StoriesUiAction

    data class ShowVariantSheet(val product: ContentTaggedProductUiModel) : StoriesUiAction

    object FetchProduct : StoriesUiAction

    object DeleteStory : StoriesUiAction
    object ContentIsLoaded : StoriesUiAction
    object SetInitialData : StoriesUiAction
    object SaveInstanceStateData : StoriesUiAction
}


enum class StoriesProductAction {
    ATC, Buy;
}
