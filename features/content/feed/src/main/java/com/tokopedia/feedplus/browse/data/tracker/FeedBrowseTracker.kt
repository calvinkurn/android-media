package com.tokopedia.feedplus.browse.data.tracker

import com.tokopedia.feedplus.browse.data.model.BannerWidgetModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.SlotInfo
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

/**
 * Created by meyta.taliti on 01/09/23.
 */

/**
 * thanos link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4134
 */
internal interface FeedBrowseTracker {

    fun viewChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    )

    fun clickChannelCard(
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        slotInfo: SlotInfo,
        channelPositionInList: Int
    )

    fun viewChipsWidget(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    )

    fun clickChipsWidget(
        item: WidgetMenuModel,
        slotInfo: SlotInfo,
        chipPositionInList: Int
    )

    fun clickBackExit()

    fun viewInspirationBanner(
        item: BannerWidgetModel,
        slotInfo: SlotInfo,
        bannerPositionInList: Int
    )

    fun clickInspirationBanner(
        item: BannerWidgetModel,
        slotInfo: SlotInfo,
        bannerPositionInList: Int
    )
}
