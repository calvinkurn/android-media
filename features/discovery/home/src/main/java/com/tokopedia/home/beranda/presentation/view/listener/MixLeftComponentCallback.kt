package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MixLeftComponentTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 09/06/20
 */
class MixLeftComponentCallback(val homeCategoryListener: HomeCategoryListener)
    : MixLeftComponentListener {

    override fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int) {
        homeCategoryListener.putEEToTrackingQueue(MixLeftComponentTracking.getMixLeftBannerView(channel, parentPos, homeCategoryListener.userId) as java.util.HashMap<String, Any>)
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int) {
        //because we have empty value at beginning of list, we need to reduce pos by 1
        val itemPos = position - 1
        //GA
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MixLeftComponentTracking.getMixLeftProductView(channel, channelGrid, itemPos, adapterPosition) as HashMap<String, Any>)
        //iris
        homeCategoryListener.putEEToIris(MixLeftComponentTracking.getMixLeftIrisProductView(channel, channelGrid, itemPos, adapterPosition)as java.util.HashMap<String, Any>)

    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int, applink: String) {
        //because we have empty value at beginning of list, we need to reduce pos by 1
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        MixLeftComponentTracking.sendMixLeftProductClick(channel, channelGrid, position - 1, adapterPosition)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        MixLeftComponentTracking.sendMixLeftSeeAllCardClick(channel, homeCategoryListener.userId)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        MixLeftComponentTracking.sendMixLeftBannerClick(channel, parentPos, homeCategoryListener.userId)
    }

    override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
    }

    override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        MixLeftComponentTracking.sendMixLeftBannerClick(channelModel, position, homeCategoryListener.userId)
    }

    override fun onSeeAllBannerClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.onDynamicChannelClicked(applink = applink)
        MixLeftComponentTracking.sendMixLeftSeeAllClick(channel, homeCategoryListener.userId)
    }
}