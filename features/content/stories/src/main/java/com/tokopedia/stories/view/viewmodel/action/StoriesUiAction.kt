package com.tokopedia.stories.view.viewmodel.action

import android.os.Bundle
import com.tokopedia.content.common.view.ContentTaggedProductUiModel
import com.tokopedia.stories.view.model.BottomSheetType

sealed interface StoriesUiAction {

    data class SetArgumentsData(val data: Bundle?) : StoriesUiAction
    data class SetGroupMainData(val selectedGroup: Int) : StoriesUiAction

    object NextDetail : StoriesUiAction

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

    data class ShowVariantSheet(val isShow: Boolean) : StoriesUiAction
}

enum class StoriesProductAction {
    ATC, Buy;
}
