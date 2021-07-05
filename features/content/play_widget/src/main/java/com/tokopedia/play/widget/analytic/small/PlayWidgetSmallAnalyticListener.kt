package com.tokopedia.play.widget.analytic.small

import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel

/**
 * Created by jegul on 19/10/20
 */
interface PlayWidgetSmallAnalyticListener {

    fun onClickViewAll(
            view: PlayWidgetSmallView
    ) {}

    fun onClickChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetSmallChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) {}

    fun onClickBannerCard(
            view: PlayWidgetSmallView
    ) {}

    fun onImpressChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetSmallChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean
    ) {}
}