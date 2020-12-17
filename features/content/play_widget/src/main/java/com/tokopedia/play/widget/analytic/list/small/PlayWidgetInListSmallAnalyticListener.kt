package com.tokopedia.play.widget.analytic.list.small

import com.tokopedia.play.widget.ui.PlayWidgetSmallView
import com.tokopedia.play.widget.ui.model.PlayWidgetSmallChannelUiModel

/**
 * Created by jegul on 02/11/20
 */
interface PlayWidgetInListSmallAnalyticListener {

    fun onClickViewAll(
            view: PlayWidgetSmallView,
            widgetPosition: Int
    ) {}

    fun onClickChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetSmallChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean,
            widgetPosition: Int
    ) {}

    fun onClickBannerCard(
            view: PlayWidgetSmallView,
            widgetPosition: Int
    ) {}

    fun onImpressChannelCard(
            view: PlayWidgetSmallView,
            item: PlayWidgetSmallChannelUiModel,
            channelPositionInList: Int,
            isAutoPlay: Boolean,
            widgetPosition: Int
    ) {}
}