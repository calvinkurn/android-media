package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * @author by yoasfs on 09/06/20
 */
class   OSMixLeftComponentCallback(private val dcEventHandler: DynamicChannelEventHandler)
    : MixLeftComponentListener {

    override fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int) {
        dcEventHandler.onMixLeftBannerImpressed(channel, parentPos)
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int) {
        //because we have empty value at beginning of list, we need to reduce pos by 1
        dcEventHandler.onFlashSaleCardImpressedComponent(position, channelGrid, channel)
    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int, applink: String) {
        //because we have empty value at beginning of list, we need to reduce pos by 1
        dcEventHandler.onFlashSaleCardClickedComponent(position, channel, channelGrid, applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        dcEventHandler.onMixFlashSaleSeeAllClickedComponent(channel, applink)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        dcEventHandler.onClickMixLeftBannerImage(channel, parentPos)
    }

    override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
        dcEventHandler.onMixLeftBannerImpressed(channelModel, position)
    }

    override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
        dcEventHandler.onClickMixLeftBannerImage(channelModel, position)
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        dcEventHandler.onSeeAllBannerClickedComponent(channel, applink)
    }
}