package com.tokopedia.play.widget.analytic.small

import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel

/**
 * Created by jegul on 19/10/20
 */
interface PlayWidgetSmallAnalyticListener {

    fun onClickViewAll(
            view: PlayWidgetSmallView
    ) {}

    fun onClickChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) {}

    fun onClickBannerCard(
            view: PlayWidgetSmallView
    ) {}

    fun onImpressChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) {}
}