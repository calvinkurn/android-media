package com.tokopedia.play.widget.analytic.list.medium

import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumOverlayUiModel

/**
 * Created by jegul on 02/11/20
 */
interface PlayWidgetInListMediumAnalyticListener {

    fun onClickViewAll(
            view: PlayWidgetMediumView,
            widgetPosition: Int
    ) {}

    fun onImpressOverlayCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumOverlayUiModel,
            channelPositionInList: Int,
            widgetPosition: Int
    ) {}

    fun onClickOverlayCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumOverlayUiModel,
            channelPositionInList: Int,
            widgetPosition: Int
    ) {}

    fun onClickChannelCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean,
            widgetPosition: Int
    ) {}

    fun onClickToggleReminderChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isRemindMe: Boolean,
            widgetPosition: Int
    ) {}

    fun onClickMoreActionChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            widgetPosition: Int
    ) {}

    fun onClickDeleteChannel(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            widgetPosition: Int
    ) {}

    fun onImpressChannelCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean,
            widgetPosition: Int
    ) {}

    fun onClickBannerCard(
            view: PlayWidgetMediumView,
            item: PlayWidgetMediumBannerUiModel,
            channelPositionInList: Int,
            widgetPosition: Int
    ) {}
}