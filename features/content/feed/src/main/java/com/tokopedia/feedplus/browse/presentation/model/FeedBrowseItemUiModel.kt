package com.tokopedia.feedplus.browse.presentation.model

import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by meyta.taliti on 11/08/23.
 */
interface FeedBrowseItemUiModel

sealed interface ChipUiState : FeedBrowseItemUiModel {

    object Placeholder : ChipUiState

    data class Data(
        val items: List<FeedBrowseChipUiModel>
    ) : ChipUiState
}

sealed interface ChannelUiState : FeedBrowseItemUiModel {

    object Placeholder : ChannelUiState

    data class Data(
        val items: List<PlayWidgetChannelUiModel>,
        val config: FeedBrowseConfigUiModel
    ) : ChannelUiState

    data class Error(
        val throwable: Throwable,
        val extraParam: WidgetRequestModel? = null
    ) : ChannelUiState
}

