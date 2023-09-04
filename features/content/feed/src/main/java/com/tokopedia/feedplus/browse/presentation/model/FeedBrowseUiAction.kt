package com.tokopedia.feedplus.browse.presentation.model

/**
 * Created by meyta.taliti on 11/08/23.
 */
sealed interface FeedBrowseUiAction {

    object LoadInitialPage: FeedBrowseUiAction

    data class FetchCards(
        val extraParams: Map<String, Any>,
        val widgetId: String
    ): FeedBrowseUiAction
}
