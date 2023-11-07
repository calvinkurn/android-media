package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by meyta.taliti on 01/09/23.
 */
internal interface FeedBrowseChannelTracker {

    fun sendViewChannelCardEvent(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    )

    fun sendClickChannelCardEvent(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    )

    fun sendViewChipsWidgetEvent(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    )

    fun sendClickChipsWidgetEvent(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    )

    interface Factory {
        fun create(prefix: String): FeedBrowseChannelTracker
    }
}
