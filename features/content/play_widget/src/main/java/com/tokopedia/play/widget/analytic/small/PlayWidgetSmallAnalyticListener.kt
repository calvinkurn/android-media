package com.tokopedia.play.widget.analytic.small

import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by jegul on 19/10/20
 */
interface PlayWidgetSmallAnalyticListener {

    /**
     * View all
     */
    fun onImpressViewAll(
        view: PlayWidgetSmallView,
    ) {}

    fun onClickViewAll(
        view: PlayWidgetSmallView,
    ) {}

    /**
     * Channel
     */
    fun onImpressChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
    ) {}

    fun onClickChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
    ) {}

    /**
     * Banner
     */
    fun onImpressBannerCard(
        view: PlayWidgetSmallView,
    ) {}

    fun onClickBannerCard(
        view: PlayWidgetSmallView,
    ) {}

    fun onLabelPromoClicked(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
    ) {}

    fun onLabelPromoImpressed(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
    ) {}
}