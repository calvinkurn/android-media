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

    fun eventImpressionMixLeftImageBanner(channel: ChannelModel, categoryName: String, bannerPosition: Int) {
        getTracker().sendEnhanceEcommerceEvent(
                BaseTrackerBuilder()
                        .appendEvent("view_item")
                        .appendEventAction(IMPRESSION_BANNER_MIX_LEFT)
                        .appendEventCategory("${OS_MICROSITE}$categoryName")
                        .appendEventLabel(channel.id)
                        .appendCustomKeyValue("promotions", createMixLeftEcommerceDataLayer(
                                channelId = channel.id,
                                categoryName = categoryName,
                                headerName = channel.channelHeader.name,
                                bannerPosition = bannerPosition,
                                creative = channel.name,
                                creativeUrl = channel.channelBanner.applink
                        ))
                        .build()
        )
    }

    fun eventClickMixLeftImageBanner(channel: ChannelModel, categoryName: String, bannerPosition: Int) {
        getTracker().sendEnhanceEcommerceEvent(
                BaseTrackerBuilder()
                        .appendEvent("select_content")
                        .appendEventAction(CLICK_BANNER_MIX_LEFT)
                        .appendEventCategory("${OS_MICROSITE}$categoryName")
                        .appendEventLabel(channel.id)
                        .appendAttribution(channel.trackingAttributionModel.galaxyAttribution)
                        .appendAffinity(channel.trackingAttributionModel.categoryPersona)
                        .appendCategoryId(channel.trackingAttributionModel.categoryId)
                        .appendShopId(channel.trackingAttributionModel.brandId)
                        .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
                        .appendCustomKeyValue("promotions", createMixLeftEcommerceDataLayer(
                                channelId = channel.id,
                                categoryName = categoryName,
                                headerName = channel.channelHeader.name,
                                bannerPosition = bannerPosition,
                                creative = channel.name,
                                creativeUrl = channel.channelBanner.applink
                        ))
                        .build()
        )
    }

    private fun createMixLeftEcommerceDataLayer(channelId: String, categoryName: String, headerName: String, bannerPosition: Int, creative: String, creativeUrl: String): ArrayList<Bundle> {
        val promotion = Bundle()
        promotion.putString("item_id", channelId)
        promotion.putString("item_name", arrayOf("$SLASH_OFFICIAL_STORE/$categoryName", VALUE_DYNAMIC_MIX_LEFT_CAROUSEL, headerName).joinToString(" - "))
        promotion.putString("creative_slot", "$bannerPosition")
        promotion.putString("creative_name", creative)
        promotion.putString("creative_url", creativeUrl)
        return arrayListOf(promotion)
    }
}