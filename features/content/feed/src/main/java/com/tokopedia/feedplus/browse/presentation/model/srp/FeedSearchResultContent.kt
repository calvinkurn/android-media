package com.tokopedia.feedplus.browse.presentation.model.srp

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by Jonathan Darwin on 25 March 2024
 */
sealed interface FeedSearchResultContent {

    data class Title(
        val title: String
    ) : FeedSearchResultContent

    data class Channel(
        val channel: PlayWidgetChannelUiModel,
        val config: PlayWidgetConfigUiModel,
    ) : FeedSearchResultContent

    object Loading : FeedSearchResultContent
}
