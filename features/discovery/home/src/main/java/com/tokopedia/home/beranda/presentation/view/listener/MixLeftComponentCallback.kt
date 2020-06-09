package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.MixLeftComponentTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.MixLeftComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener

/**
 * @author by yoasfs on 09/06/20
 */
class MixLeftComponentCallback(val homeCategoryListener: HomeCategoryListener)
    : CommonProductCardCarouselListener, MixLeftComponentListener {

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        //because we have empty value at beginning of list, we need to reduce pos by 1
        val itemPos = position - 1
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                MixLeftComponentTracking.getMixLeftProductView(channel, channelGrid, itemPos) as HashMap<String, Any>)
    }

    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) {
        //because we have empty value at beginning of list, we need to reduce pos by 1
        homeCategoryListener.openApplink(applink = applink, trackingAttribution =  "")
        MixLeftComponentTracking.sendMixLeftProductClick(channel, channelGrid, position - 1)
    }

    override fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) {
        homeCategoryListener.openApplink(applink = applink, trackingAttribution =  "")
        MixLeftComponentTracking.sendMixLeftSeeAllCardClick(channel, homeCategoryListener.userId)
    }

    override fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) {
        homeCategoryListener.openApplink(applink = applink, trackingAttribution =  "")
        MixLeftComponentTracking.sendMixLeftBannerClick(channel, parentPos)
    }

    override fun onImageBannerImpressed(channelModel: ChannelModel, position: Int) {
        homeCategoryListener.putEEToTrackingQueue(MixLeftComponentTracking.getMixLeftBannerView(channelModel, position) as java.util.HashMap<String, Any>)
    }

    override fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String) {
        homeCategoryListener.openApplink(applink = applink, trackingAttribution =  "")
        MixLeftComponentTracking.sendMixLeftBannerClick(channelModel, position)
    }
}