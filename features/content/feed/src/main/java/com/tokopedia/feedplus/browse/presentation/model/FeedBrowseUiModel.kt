package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView

/**
 * Created by meyta.taliti on 11/08/23.
 */
sealed interface FeedBrowseUiModel {

    val id: String

    data class Placeholder(
        val type: FeedBrowsePlaceholderView.Type
    ) : FeedBrowseUiModel {
        override val id: String = type.ordinal.toString()
    }

    data class Channel(
        override val id: String,
        val title: String,
        val extraParam: WidgetRequestModel,
        val chipUiState: ChipUiState,
        val channelUiState: ChannelUiState
    ) : FeedBrowseUiModel

    data class Title(
        val slotId: String,
        val title: String,
    ) : FeedBrowseUiModel {
        override val id = "${slotId}_title_${title}"
    }

    data class Banner(
        val slotId: String,
        val title: String,
        val imageUrl: String,
        val appLink: String,
    ) : FeedBrowseUiModel {
        override val id = "${slotId}_banner_${title}"
    }
}
