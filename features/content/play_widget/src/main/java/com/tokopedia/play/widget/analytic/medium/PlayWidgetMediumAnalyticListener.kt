package com.tokopedia.play.widget.analytic.medium

import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel


/**
 * Created by mzennis on 26/10/20.
 */
interface PlayWidgetMediumAnalyticListener {

    fun onClickViewAll(
            view: PlayWidgetMediumView
    ) {}

    fun onImpressOverlayCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumOverlayUiModel,
            channelPositionInList: Int
    ) {}

    fun onClickOverlayCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumOverlayUiModel,
            channelPositionInList: Int
    ) {}

    fun onClickChannelCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) {}

    fun onClickToggleReminderChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isRemindMe: Boolean
    ) {}

    fun onClickMenuActionChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int
    ) {}

    fun onClickDeleteChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int
    ) {}

    fun onImpressChannelCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) {}

    fun onClickBannerCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumBannerUiModel,
            channelPositionInList: Int
    ) {}
}