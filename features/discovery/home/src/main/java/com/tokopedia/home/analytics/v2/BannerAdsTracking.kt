package com.tokopedia.home.analytics.v2

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.track.TrackApp
import com.tokopedia.trackingoptimizer.TrackingQueue

object BannerAdsTracking : BaseTracking() {
    private class CustomAction{
        companion object {
            val IMPRESSION_ON_BANNER_ADS = Action.IMPRESSION_ON.format("banner dynamic channel ads")
            val CLICK_ON_BANNER_ADS = Action.CLICK_ON.format("banner dynamic channel ads")
        }
    }

    private fun getBannerAdsImpression(
            userId: String,
            isToIris: Boolean = false,
            channel: DynamicHomeChannel.Channels,
            position: Int
    ) = getBasicPromotionChannelView(
            event = if (isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE_TOPADS,
            eventAction = CustomAction.IMPRESSION_ON_BANNER_ADS,
            eventLabel = "",
            channelId = channel.id,
            userId = userId,
            promotions = listOf(
                    Promotion(
                            id = channel.id,
                            name = channel.promoName,
                            creative = "",
                            position = (position+1).toString()
                    )
            ),
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            screen = Screen.DEFAULT
    )

    private fun getBannerAdsClick(
            headerName: String,
            channelId: String,
            userId: String,
            position: Int,
            channel: DynamicHomeChannel.Channels
    ) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE_TOPADS,
            eventAction = CustomAction.CLICK_ON_BANNER_ADS,
            eventLabel = "",
            channelId = channelId,
            affinity = channel.persona,
            attribution = channel.galaxyAttribution,
            categoryId = channel.categoryID,
            shopId = channel.brandId,
            userId = userId,
            campaignCode = channel.campaignCode,
            promotions = listOf(
                    Promotion(
                            id = channel.id,
                            name = channel.promoName,
                            creative = "",
                            position = (position+1).toString()
                    )
            ),
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            screen = Screen.DEFAULT
    )

    fun sendBannerAdsClickTracking(channelModel: DynamicHomeChannel.Channels,
                                   userId: String,
                                   position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getBannerAdsClick(
                        channelModel.header.name,
                        channelModel.id,
                        userId,
                        position,
                        channelModel
                )
        )
    }

    fun sendBannerAdsImpressionTracking(trackingQueue: TrackingQueue?,
                                   channelModel: DynamicHomeChannel.Channels,
                                   userId: String,
                                   position: Int, isToIris: Boolean = false) {
        trackingQueue?.putEETracking(
                getBannerAdsImpression(
                        userId,
                        isToIris,
                        channelModel,
                        position
                ) as HashMap<String, Any>
        )
    }
}