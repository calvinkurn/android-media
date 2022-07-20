package com.tokopedia.play.widget.analytic.small

import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel

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
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {}

    fun onClickChannelCard(
        view: PlayWidgetSmallView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
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
}