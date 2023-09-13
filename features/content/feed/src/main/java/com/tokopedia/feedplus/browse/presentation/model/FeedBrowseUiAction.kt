package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
sealed interface FeedBrowseUiAction {

    object LoadInitialPage : FeedBrowseUiAction

    data class SelectChip(
        val model: FeedBrowseChipUiModel,
        val widgetId: String
    ) : FeedBrowseUiAction

    data class FetchCards(
        val extraParam: WidgetRequestModel,
        val widgetId: String
    ) : FeedBrowseUiAction
}
