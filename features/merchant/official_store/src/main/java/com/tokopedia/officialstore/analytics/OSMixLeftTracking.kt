package com.tokopedia.officialstore.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.interfaces.ContextAnalytics

object OSMixLeftTracking: BaseTrackerConst() {

    private val OS_MICROSITE = "os microsite - "
    private val SLASH_OFFICIAL_STORE = "/official-store"
    private val IMPRESSION_BANNER_MIX_LEFT = "impression banner dynamic channel left carousel"
    private val CLICK_BANNER_MIX_LEFT = "click banner dynamic channel left carousel"
    private val VALUE_DYNAMIC_MIX_LEFT_CAROUSEL = "dynamic channel left carousel"
    private val SKEL_APPLINK = "{&data}"
    private val SKEL_APPLINK_DATA = "&data"

    fun eventImpressionMixLeftImageBanner(channel: ChannelModel, categoryName: String, bannerPosition: Int) =
            BaseTrackerBuilder()
                    .constructBasicPromotionView(
                            event = Event.PROMO_VIEW,
                            eventAction = IMPRESSION_BANNER_MIX_LEFT,
                            eventCategory = "${OS_MICROSITE}${categoryName.toLowerCase()}",
                            eventLabel = channel.id,
                            promotions = listOf(createMixLeftEcommerceDataLayer(
                                    channelId = channel.id,
                                    categoryName = categoryName.toLowerCase(),
                                    headerName = channel.channelHeader.name,
                                    bannerPosition = bannerPosition,
                                    creative = channel.name,
                                    creativeUrl = channel.channelBanner.applink
                            )))
                    .build()

    fun eventClickMixLeftImageBanner(channel: ChannelModel, categoryName: String, bannerPosition: Int) =
            BaseTrackerBuilder()
                    .constructBasicPromotionClick(
                            event = Event.PROMO_CLICK,
                            eventAction = CLICK_BANNER_MIX_LEFT,
                            eventCategory = "${OS_MICROSITE}${categoryName.toLowerCase()}",
                            eventLabel = channel.id,
                            promotions = listOf(createMixLeftEcommerceDataLayer(
                                    channelId = channel.id,
                                    categoryName = categoryName.toLowerCase(),
                                    headerName = channel.channelHeader.name,
                                    bannerPosition = bannerPosition,
                                    creative = channel.name,
                                    creativeUrl = channel.channelBanner.applink
                            )))
                    .appendAttribution(channel.trackingAttributionModel.galaxyAttribution)
                    .appendAffinity(channel.trackingAttributionModel.categoryPersona)
                    .appendCategoryId(channel.trackingAttributionModel.categoryId)
                    .appendShopId(channel.trackingAttributionModel.brandId)
                    .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
                    .build()


    private fun createMixLeftEcommerceDataLayer(channelId: String, categoryName: String, headerName: String, bannerPosition: Int, creative: String, creativeUrl: String): Promotion{
        return Promotion(
                id = channelId,
                name = arrayOf("$SLASH_OFFICIAL_STORE/$categoryName", VALUE_DYNAMIC_MIX_LEFT_CAROUSEL, headerName, SKEL_APPLINK.replace(SKEL_APPLINK_DATA, creativeUrl)).joinToString(" - "),
                position = "$bannerPosition",
                creative = creative,
                creativeUrl = creativeUrl
        )

    }
}