package com.tokopedia.play.widget.analytic.carousel

import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetProduct

/**
 * Created by kenny.hadisaputra on 24/05/23
 */
interface PlayWidgetCarouselAnalyticListener {

    fun onImpressChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {}

    fun onClickChannelCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {}

    fun onClickPartnerName(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {}

    fun onClickToggleMuteButton(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
    ) {}

    fun onClickToggleReminderChannel(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        channelPositionInList: Int,
        isRemindMe: Boolean,
    ) {}

    fun onImpressProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
    ) {}

    fun onClickProductCard(
        view: PlayWidgetCarouselView,
        item: PlayWidgetChannelUiModel,
        config: PlayWidgetConfigUiModel,
        product: PlayWidgetProduct,
        productPosition: Int,
        channelPositionInList: Int,
    ) {}
}
