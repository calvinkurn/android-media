package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
open class FeedBrowseItemUiModel

sealed class ChipUiState : FeedBrowseItemUiModel() {

    object Placeholder : ChipUiState()

    data class Data(
        val items: List<FeedBrowseChipUiModel>
    ) : ChipUiState()
}

sealed class ChannelUiState : FeedBrowseItemUiModel() {

    object Placeholder : ChannelUiState()

    data class Data(
        val items: List<PlayWidgetChannelUiModel>,
        val config: FeedBrowseConfigUiModel
    ) : ChannelUiState()

    data class Error(
        val throwable: Throwable,
        val extraParams: Map<String, Any>? = null
    ) : ChannelUiState()
}
