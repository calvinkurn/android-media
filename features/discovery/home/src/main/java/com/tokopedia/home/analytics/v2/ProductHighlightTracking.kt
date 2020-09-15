package com.tokopedia.home.analytics.v2

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ProductHighlightTracking : BaseTrackerConst() {
    private const val EVENT_ACTION_IMPRESSION_PRODUCT_DYNAMIC_CHANNEL_HERO = "impression on product dynamic channel hero"
    private const val EVENT_ACTION_CLICK_PRODUCT_DYNAMIC_CHANNEL_HERO = "click on product dynamic channel hero"

    private const val PRODUCT_DYNAMIC_CHANNEL_HERO = "dynamic channel hero"

    fun sendRecommendationListClick(
            channelId: String,
            headerName: String,
            campaignCode: String,
            persoType: String,
            categoryId: String,
            gridId: String,
            gridName: String,
            gridPrice: String,
            gridFreeOngkirIsActive: Boolean,
            position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getProductHighlightClick(
                channelId, headerName, campaignCode, persoType, categoryId, gridId, gridName, gridPrice, gridFreeOngkirIsActive, position))
    }

    //componentSection
    fun getProductHighlightImpression(channel: ChannelModel, userId: String = "", isToIris: Boolean = false): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_IMPRESSION_PRODUCT_DYNAMIC_CHANNEL_HERO,
                eventLabel = Label.NONE,
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", PRODUCT_DYNAMIC_CHANNEL_HERO, channel.channelHeader.name
                ),
                products = channel.channelGrids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.isFreeOngkirActive,
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            isTopAds = grid.isTopads,
                            isCarousel = false,
                            headerName = channel.header.name,
                            recommendationType = grid.recommendationType
                    )
                })
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .build()
    }

    private fun getProductHighlightClick(
            channelId: String,
            headerName: String,
            campaignCode: String,
            persoType: String,
            categoryId: String,
            gridId: String,
            gridName: String,
            gridPrice: String,
            gridFreeOngkirIsActive: Boolean,
            isTopAds: Boolean,
            recommendationType: String,
            position: Int) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_CLICK_PRODUCT_DYNAMIC_CHANNEL_HERO,
                eventLabel = "$channelId - $headerName",
                products = listOf(
                        Product(
                                id = gridId,
                                name = gridName,
                                productPrice = convertRupiahToInt(gridPrice).toString(),
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                productPosition = (position + 1).toString(),
                                channelId = channelId,
                                isFreeOngkir = gridFreeOngkirIsActive,
                                persoType = persoType,
                                categoryId = categoryId,
                                isTopAds = isTopAds,
                                isCarousel = false,
                                headerName = headerName,
                                recommendationType = recommendationType
                        )
                ),
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", PRODUCT_DYNAMIC_CHANNEL_HERO, headerName
                ))
                .appendChannelId(channelId)
                .appendCampaignCode(campaignCode)
                .build()
    }


    //end componentSection
}