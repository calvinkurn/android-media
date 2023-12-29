package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView

/**
 * Created by meyta.taliti on 11/08/23.
 */
sealed class FeedBrowseUiModel {

    data class Placeholder(
        val type: FeedBrowsePlaceholderView.Type
    ) : FeedBrowseUiModel()

    data class Channel(
        val id: String,
        val title: String,
        val extraParam: WidgetRequestModel,
        val chipUiState: ChipUiState,
        val channelUiState: ChannelUiState
    ) : FeedBrowseUiModel()
}
