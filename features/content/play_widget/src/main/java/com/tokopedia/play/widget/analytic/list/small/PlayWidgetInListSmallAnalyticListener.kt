package com.tokopedia.play.widget.analytic.list.small

import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by jegul on 02/11/20
 */
interface PlayWidgetInListSmallAnalyticListener {

    fun onClickViewAll(
        view: PlayWidgetSmallView,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
    }

    fun onClickChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
    }

    fun onClickBannerCard(
        view: PlayWidgetSmallView,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
    }

    fun onImpressChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        isAutoPlay: Boolean,
        verticalWidgetPosition: Int,
        businessWidgetPosition: Int,
    ) {
    }

    fun onLabelPromoClicked(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        businessWidgetPosition: Int,
        isAutoPlay: Boolean,
    ) {}

    fun onLabelPromoImpressed(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        channelPositionInList: Int,
        businessWidgetPosition: Int,
        isAutoPlay: Boolean,
    ) {}
}