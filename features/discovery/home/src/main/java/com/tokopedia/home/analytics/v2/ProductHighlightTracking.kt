package com.tokopedia.home.analytics.v2

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

object ProductHighlightTracking : BaseTracking() {
    private const val EVENT_ACTION_IMPRESSION_PRODUCT_DYNAMIC_CHANNEL_HERO = "impression on product dynamic channel hero"
    private const val EVENT_ACTION_CLICK_PRODUCT_DYNAMIC_CHANNEL_HERO = "click on product dynamic channel hero"

    private const val PRODUCT_DYNAMIC_CHANNEL_HERO = "dynamic channel hero"

    fun getProductHighlightImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = getBasicProductChannelView(
            event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_IMPRESSION_PRODUCT_DYNAMIC_CHANNEL_HERO,
            eventLabel = Label.NONE,
            products = channel.grids.mapIndexed { index, grid ->
                Product(
                        name = grid.name,
                        id = grid.id,
                        productPrice = convertRupiahToInt(grid.price).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (index + 1).toString(),
                        channelId = channel.id,
                        isFreeOngkir = grid.freeOngkir.isActive,
                        persoType = channel.persoType,
                        categoryId = channel.categoryID
                )
            },
            list = String.format(
                    Value.LIST_WITH_HEADER, "1", PRODUCT_DYNAMIC_CHANNEL_HERO, channel.header.name
            ),
            channelId = channel.id
    )

    private fun getProductHighlightClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_PRODUCT_DYNAMIC_CHANNEL_HERO,
            eventLabel = channel.header.name,
            channelId = channel.id,
            campaignCode = channel.campaignCode,
            products = listOf(
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (position + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.freeOngkir.isActive,
                            persoType = channel.persoType,
                            categoryId = channel.categoryID
                    )
            ),
            list = String.format(
                    Value.LIST_WITH_HEADER, "1", PRODUCT_DYNAMIC_CHANNEL_HERO, channel.header.name
            )
    )

    fun sendRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) {
        getTracker().sendEnhanceEcommerceEvent(getProductHighlightClick(channel, grid, position))
    }
}