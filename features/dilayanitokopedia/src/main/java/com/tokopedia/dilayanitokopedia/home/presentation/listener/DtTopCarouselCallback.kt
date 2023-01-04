package com.tokopedia.dilayanitokopedia.home.presentation.listener

import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by irpan on 16/11/22.
 */
class DtTopCarouselCallback {

    fun createTopCarouselCallback(onActionLinkClicked: (String) -> Unit): MixTopComponentListener? {
        return object : MixTopComponentListener {
            override fun onMixTopImpressed(channel: ChannelModel, parentPos: Int) {
                // no-op
            }

            override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
                onActionLinkClicked(channel.channelHeader.applink)
            }

            override fun onMixtopButtonClicked(channel: ChannelModel) {
                // no-op
            }

            override fun onSectionItemClicked(applink: String) {
                onActionLinkClicked(applink)
            }

            override fun onBackgroundClicked(channel: ChannelModel) {
                onActionLinkClicked(channel.channelHeader.applink)
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
                onActionLinkClicked(channelGrid.applink)
            }

            override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
                onActionLinkClicked(applink)
            }
        }
    }
}
