package com.tokopedia.dilayanitokopedia.ui.home.adapter.listener

import com.tokopedia.dilayanitokopedia.data.analytics.DtHomepageAnalytics
import com.tokopedia.dilayanitokopedia.data.analytics.ProductCardAnalyticsMapper
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.user.session.UserSessionInterface

object DtLeftCarouselCallback {

    fun createLeftCarouselCallback(
        userSession: UserSessionInterface,
        onActionLinkClicked: (String) -> Unit
    ): MixLeftComponentListener {
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
                DtHomepageAnalytics.sendImpressionProductCardsDtEvent(
                    userSession,
                    ProductCardAnalyticsMapper.fromGridChannel(position, channelGrid)
                )
            }

            override fun onProductCardClicked(
                channel: ChannelModel,
                channelGrid: ChannelGrid,
                adapterPosition: Int,
                position: Int,
                applink: String
            ) {
                onActionLinkClicked(applink)
                DtHomepageAnalytics.sendClickProductCardsDtEvent(
                    userSession,
                    ProductCardAnalyticsMapper.fromGridChannel(position, channelGrid)
                )
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
