package com.tokopedia.play.widget.analytic.list.carousel

import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct

/**
 * Created by kenny.hadisaputra on 24/05/23
 */
interface PlayWidgetInListCarouselAnalyticListener {

    fun onImpressChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {}

    fun onClickChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {}

    fun onClickPartnerName(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {}

    fun onClickToggleMuteButton(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {}

    fun onClickToggleReminderChannel(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
        verticalWidgetPosition: Int,
    ) {}

    fun onImpressProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {}

    fun onClickProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
        verticalWidgetPosition: Int,
    ) {}
}
