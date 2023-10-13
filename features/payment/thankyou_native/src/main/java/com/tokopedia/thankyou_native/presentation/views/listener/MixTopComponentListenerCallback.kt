package com.tokopedia.thankyou_native.presentation.views.listener

import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.thankyou_native.analytics.FlashSaleWidgetTracking

class MixTopComponentListenerCallback(
    private val flashSaleWidgetListener: FlashSaleWidgetListener
    ): MixTopComponentListener {
    override fun onMixTopImpressed(channel: ChannelModel, parentPos: Int) {

    }

    override fun onSeeAllHeaderClicked(channel: ChannelModel, applink: String) {
        FlashSaleWidgetTracking().sendFlashSaleWidgetLoadMoreCard(
            channel,
            flashSaleWidgetListener.getUserId()
        )
        flashSaleWidgetListener.route(applink)
    }

    override fun onMixtopButtonClicked(channel: ChannelModel) {

    }

    override fun onSectionItemClicked(applink: String) {

    }

    override fun onBackgroundClicked(channel: ChannelModel) {

    }

    override fun onProductCardImpressed(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int
    ) {
        flashSaleWidgetListener.getTrackingQueueObj()?.putEETracking(
            FlashSaleWidgetTracking().getMixLeftProductView(
                channel,
                channelGrid,
                position,
                flashSaleWidgetListener.getFlashSaleWidgetPosition(),
                flashSaleWidgetListener.getUserId()
            ) as HashMap<String, Any>
        )
    }

    override fun onProductCardClicked(
        channel: ChannelModel,
        channelGrid: ChannelGrid,
        adapterPosition: Int,
        position: Int,
        applink: String
    ) {
        FlashSaleWidgetTracking().sendProductClick(
            channel,
            channelGrid,
            position,
            flashSaleWidgetListener.getFlashSaleWidgetPosition(),
            flashSaleWidgetListener.getUserId()
        )
        flashSaleWidgetListener.route(applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        FlashSaleWidgetTracking().sendFlashSaleWidgetLoadMoreCard(
            channel,
            flashSaleWidgetListener.getUserId()
        )
        flashSaleWidgetListener.route(applink)
    }
}
