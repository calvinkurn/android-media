package com.tokopedia.officialstore.official.presentation.listener

import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Cta
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelEventHandler

/**
 * Created by Lukas on 3/18/21.
 */
class OSMixTopComponentCallback (private val dcEventHandler: DynamicChannelEventHandler): MixTopComponentListener{
    override fun onMixTopImpressed(channel: ChannelModel, parentPos: Int) {

    }

    override fun onSeeAllHeaderClicked(channel: ChannelModel, applink: String) {
        dcEventHandler.onCarouselSeeAllHeaderClicked(channel, applink)
    }

    override fun onMixtopButtonClicked(channel: ChannelModel) {
        dcEventHandler.onClickMixTopBannerCtaButton(
                Cta(
                        channel.channelBanner.cta.type,
                        channel.channelBanner.cta.mode,
                        channel.channelBanner.cta.text,
                        channel.channelBanner.cta.couponCode
                ),
                channel.id,
                channel.channelBanner.applink,
                channel.channelHeader.name,
                channel.channelBanner.attribution
        )
    }

    override fun onSectionItemClicked(applink: String) {
        dcEventHandler.onClickMixTopBannerItem(applink)
    }

    override fun onBackgroundClicked(channel: ChannelModel) {
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int) {
        dcEventHandler.onProductCardImpressed(position, channelGrid, channel)
    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int, applink: String) {
        dcEventHandler.onProductCardClicked(position + 1, channel, channelGrid, applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        dcEventHandler.onCarouselSeeAllCardClicked(channel, applink)
    }
}
