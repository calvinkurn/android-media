package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object ProductHighlightTracking : BaseTrackerConst() {
    private const val EVENT_ACTION_IMPRESSION_PRODUCT_DYNAMIC_CHANNEL_HERO = "impression on product dynamic channel hero"
    private const val EVENT_ACTION_CLICK_PRODUCT_DYNAMIC_CHANNEL_HERO = "click on product dynamic channel hero"

    private const val PRODUCT_DYNAMIC_CHANNEL_HERO = "dynamic channel hero - product"

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
            gridFreeOngkirExtraIsActive: Boolean,
            position: Int,
            isTopAds: Boolean,
            recommendationType: String,
            pageName: String,
            positionOnHome: Int) {
        getTracker().sendEnhanceEcommerceEvent(getProductHighlightClick(
                channelId,
                headerName,
                campaignCode,
                persoType,
                categoryId,
                gridId,
                gridName,
                gridPrice,
                gridFreeOngkirIsActive,
                gridFreeOngkirExtraIsActive,
                isTopAds,
                recommendationType,
                pageName,
                position,
                positionOnHome))
    }

    //componentSection
    fun getProductHighlightImpression(channel: ChannelModel, userId: String = "", isToIris: Boolean = false, positionOnHome: Int): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicProductView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_IMPRESSION_PRODUCT_DYNAMIC_CHANNEL_HERO,
                eventLabel = Label.NONE,
                list = String.format(
                        Value.LIST, positionOnHome, PRODUCT_DYNAMIC_CHANNEL_HERO
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
                            isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                            isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            isTopAds = grid.isTopads,
                            isCarousel = false,
                            headerName = channel.channelHeader.name,
                            recommendationType = grid.recommendationType,
                            pageName = channel.pageName
                    )
                })
                .appendChannelId(channel.id)
                .appendUserId(userId)
                .appendScreen(Screen.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
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
            gridFreeOngkirExtraIsActive: Boolean,
            isTopAds: Boolean,
            recommendationType: String,
            pageName: String,
            position: Int,
            positionOnHome: Int) : Map<String, Any> {
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
                                isFreeOngkirExtra = gridFreeOngkirExtraIsActive,
                                persoType = persoType,
                                categoryId = categoryId,
                                isTopAds = isTopAds,
                                isCarousel = false,
                                headerName = headerName,
                                recommendationType = recommendationType,
                                pageName = pageName
                        )
                ),
                list = String.format(
                        Value.LIST, positionOnHome, PRODUCT_DYNAMIC_CHANNEL_HERO
                ))
                .appendChannelId(channelId)
                .appendCampaignCode(campaignCode)
                .appendScreen(Screen.DEFAULT)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .build()
    }


    //end componentSection
}