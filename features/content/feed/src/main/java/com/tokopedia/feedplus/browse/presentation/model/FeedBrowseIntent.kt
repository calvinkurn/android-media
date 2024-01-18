package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal sealed interface FeedBrowseIntent {

    object LoadInitialPage : FeedBrowseIntent

    data class SelectChipWidget(
        val slotId: String,
        val model: WidgetMenuModel
    ) : FeedBrowseIntent

    data class FetchCardsWidget(
        val slotId: String,
        val model: WidgetMenuModel
    ) : FeedBrowseIntent

    object UpdateStoriesStatus : FeedBrowseIntent
}
