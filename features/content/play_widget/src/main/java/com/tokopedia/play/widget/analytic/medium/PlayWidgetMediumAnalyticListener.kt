package com.tokopedia.play.widget.analytic.medium

import com.tokopedia.play.widget.ui.PlayWidgetMediumView
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetBannerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel


/**
 * Created by mzennis on 26/10/20.
 */
interface PlayWidgetMediumAnalyticListener {

    /**
     * View all
     */
    fun onImpressViewAll(
        view: PlayWidgetMediumView
    ) {}

    fun onClickViewAll(
        view: PlayWidgetMediumView
    ) {}

    /**
     * Banner
     */
    fun onImpressBannerCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int,
    ) {}

    fun onClickBannerCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBannerUiModel,
        channelPositionInList: Int
    ) {}

    /**
     * Overlay
     */
    fun onImpressOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int
    ) {}

    fun onClickOverlayCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetBackgroundUiModel,
        channelPositionInList: Int
    ) {}

    /**
     * Reminder
     */
    fun onImpressReminderIcon(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isReminded: Boolean,
    ) {}

    fun onClickToggleReminderChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean
    ) {}

    /**
     * Channel
     * channelPositionInList value start with 1 not 0
     */
    fun onImpressChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
    }

    fun onClickChannelCard(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {
    }

    fun onClickMenuActionChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int
    ) {
    }

    fun onClickDeleteChannel(
        view: PlayWidgetMediumView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int
    ) {
    }
}
