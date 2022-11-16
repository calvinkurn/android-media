package com.tokopedia.dilayanitokopedia.home.presentation.listener

import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

object DtLeftCarouselCallback {

    fun createLeftCarouselCallback(onActionLinkClicked: (String) -> Unit): MixLeftComponentListener {
        return object : MixLeftComponentListener {

            override fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int) {
                // no-op
            }

            override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
                // no-op
            }

            override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
                onActionLinkClicked(applink)
            }

            override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
                onActionLinkClicked(applink)
            }

            override fun onProductCardImpressed(
                channel: ChannelModel,
                channelGrid: ChannelGrid,
                adapterPosition: Int,
                position: Int
            ) {
                // no-op
            }

            override fun onProductCardClicked(
                channel: ChannelModel,
                channelGrid: ChannelGrid,
                adapterPosition: Int,
                position: Int,
                applink: String
            ) {
                onActionLinkClicked(applink)
            }

            override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
                onActionLinkClicked(applink)
            }

            override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
                onActionLinkClicked(applink)
            }
        }
    }
}
