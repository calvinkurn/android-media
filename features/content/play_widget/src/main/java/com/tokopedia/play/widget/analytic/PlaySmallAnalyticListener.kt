package com.tokopedia.play.widget.analytic

import com.tokopedia.play.widget.ui.PlayWidgetSmallView

/**
 * Created by jegul on 19/10/20
 */
interface PlaySmallAnalyticListener {

    fun onClickViewAll(
            view: PlayWidgetSmallView
    ) {}

    fun onClickChannelCard(
            view: PlayWidgetSmallView,
            channelPositionInList: Int
    ) {}

    fun onClickBannerCard(
            view: PlayWidgetSmallView
    ) {}

    fun onImpressChannelCard(
            view: PlayWidgetSmallView,
            channelPositionInList: Int
    ) {}
}