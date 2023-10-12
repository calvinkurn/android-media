package com.tokopedia.dilayanitokopedia.ui.home.adapter.listener

import com.tokopedia.dilayanitokopedia.data.analytics.DtHomepageAnalytics
import com.tokopedia.dilayanitokopedia.data.analytics.ProductCardAnalyticsMapper
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by irpan on 16/11/22.
 */
class DtTopCarouselCallback {

    fun createTopCarouselCallback(
        userSession: UserSessionInterface,
        onActionLinkClicked: (String) -> Unit
    ): MixTopComponentListener? {
        return object : MixTopComponentListener {
            override fun onMixTopImpressed(channel: ChannelModel, parentPos: Int) {
                // no-op
            }

            override fun onSeeAllHeaderClicked(channel: ChannelModel, applink: String) {
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
                onActionLinkClicked(channelGrid.applink)
                DtHomepageAnalytics.sendClickProductCardsDtEvent(
                    userSession,
                    ProductCardAnalyticsMapper.fromGridChannel(position, channelGrid)
                )
            }

            override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
                onActionLinkClicked(applink)
            }
        }
    }
}
