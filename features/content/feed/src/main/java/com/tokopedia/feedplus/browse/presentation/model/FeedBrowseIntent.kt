package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal sealed interface FeedBrowseIntent {

    object LoadInitialPage : FeedBrowseIntent

    data class SelectChip(
        val model: FeedBrowseChipUiModel,
        val widgetId: String
    ) : FeedBrowseIntent

    data class SelectChipWidget(
        val slotId: String,
        val model: WidgetMenuModel,
    ) : FeedBrowseIntent

    data class FetchCards(
        val extraParam: WidgetRequestModel,
        val widgetId: String
    ) : FeedBrowseIntent

    data class FetchCardsWidget(
        val slotId: String,
        val model: WidgetMenuModel,
    ) : FeedBrowseIntent
}
