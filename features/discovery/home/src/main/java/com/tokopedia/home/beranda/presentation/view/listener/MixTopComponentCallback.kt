package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MixTopTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MixTopComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 11/06/20
 */
class MixTopComponentCallback(val homeCategoryListener: HomeCategoryListener)
    : MixTopComponentListener {
    override fun onMixTopImpressed(channel: ChannelModel, parentPos: Int) {
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopSeeAllClick(channel.id, channel.channelHeader.name, homeCategoryListener.userId) as HashMap<String, Any>)
        homeCategoryListener.onDynamicChannelClicked(channel.channelHeader.applink)
    }

    override fun onMixtopButtonClicked(channel: ChannelModel) {
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopButtonClick(channel.id, channel.channelHeader.name, channel.channelBanner.cta.text, homeCategoryListener.userId) as java.util.HashMap<String, Any>)
    }

    override fun onSectionItemClicked(applink: String) {
        homeCategoryListener.onSectionItemClicked(applink)
    }

    override fun onBackgroundClicked(channel: ChannelModel) {
        homeCategoryListener.onDynamicChannelClicked(channel.channelBanner.applink)
        homeCategoryListener.sendEETracking(MixTopTracking.getBackgroundClickComponent(channel, homeCategoryListener.userId) as java.util.HashMap<String, Any>)
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int) {
        //GA
        val product = MixTopTracking.mapGridToProductTrackerComponent(
                channelGrid, channel.id, position+1, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId, channel.channelHeader.name, channel.pageName)
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MixTopTracking.getMixTopView(
                        listOf(product),
                        adapterPosition.toString()
                ) as HashMap<String, Any>)
        
        //iris
        homeCategoryListener.putEEToIris(MixTopTracking.getMixTopViewIris(
                listOf(product),
                channel.channelHeader.name,
                channel.id,
                adapterPosition.toString()
        ) as java.util.HashMap<String, Any>)

    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int, applink: String) {
        val product = MixTopTracking.mapGridToProductTrackerComponent(
                channelGrid, channel.id, position+1, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId, channel.channelHeader.name, channel.pageName)
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopClick(
                listOf(product),
                channel.channelHeader.name,
                channel.id,
                adapterPosition.toString(),
                channel.trackingAttributionModel.campaignCode
        ) as HashMap<String, Any>)
        homeCategoryListener.onDynamicChannelClicked(channelGrid.applink)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.sendEETracking(MixTopTracking.getMixTopSeeAllCardClick(channel.id, channel.channelHeader.name, homeCategoryListener.userId) as HashMap<String, Any>)
        homeCategoryListener.onDynamicChannelClicked(applink)
    }
}