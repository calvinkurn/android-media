package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by meyta.taliti on 01/09/23.
 */
interface FeedBrowseChannelTracker {

    fun sendViewChannelCardEvent(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widget: FeedBrowseUiModel.Channel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    )

    fun sendViewChipsWidgetEvent(
        item: FeedBrowseChipUiModel,
        widget: FeedBrowseUiModel.Channel,
        chipPositionInList: Int,
        verticalWidgetPosition: Int
    )

    fun sendClickChannelCardEvent(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        widget: FeedBrowseUiModel.Channel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int
    )

    fun sendClickChipsWidgetEvent(
        item: FeedBrowseChipUiModel,
        widget: FeedBrowseUiModel.Channel,
        chipPositionInList: Int,
        verticalWidgetPosition: Int
    )

    interface Factory {
        fun create(prefix: String): FeedBrowseChannelTracker
    }
}
