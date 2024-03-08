package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal sealed interface FeedBrowseAction {

    object LoadInitialPage : FeedBrowseAction

    data class SelectChipWidget(
        val slotId: String,
        val model: WidgetMenuModel
    ) : FeedBrowseAction

    data class FetchCardsWidget(
        val slotId: String,
        val model: WidgetMenuModel
    ) : FeedBrowseAction

    object UpdateStoriesStatus : FeedBrowseAction
}
