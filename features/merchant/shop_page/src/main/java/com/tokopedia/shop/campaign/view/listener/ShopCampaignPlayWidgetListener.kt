package com.tokopedia.shop.campaign.view.listener

import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel

interface ShopCampaignPlayWidgetListener {

    fun onImpressionPlayWidget(
        widgetModel: CarouselPlayWidgetUiModel,
        position: Int
    )
    fun onPlayWidgetItemImpression(
        widgetModel: CarouselPlayWidgetUiModel,
        channelModel: PlayWidgetChannelUiModel,
        position: Int
    )

    fun onPlayWidgetItemClick(
        widgetModel: CarouselPlayWidgetUiModel,
        channelModel: PlayWidgetChannelUiModel,
        position: Int
    )
}
