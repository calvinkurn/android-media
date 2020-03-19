package com.tokopedia.home.analytics.v2

import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

object ProductHighlightTracking : BaseTracking() {
    val PRODUCT_DYNAMIC_CHANNEL_HERO = "product dynamic channel hero"
    val PRODUCT_DYNAMIC_CHANNEL_HERO_IMPRESSION = Action.IMPRESSION_ON.format(PRODUCT_DYNAMIC_CHANNEL_HERO)
    val PRODUCT_DYNAMIC_CHANNEL_HERO_CLICK = Action.CLICK_ON.format(PRODUCT_DYNAMIC_CHANNEL_HERO)

    fun getProductHighlightImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = getBasicProductView(
            event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = PRODUCT_DYNAMIC_CHANNEL_HERO_IMPRESSION,
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
            )
    )

    private fun getProductHighlightClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = PRODUCT_DYNAMIC_CHANNEL_HERO_CLICK,
            eventLabel = grid.attribution,
            channelId = channel.id,
            products = listOf(
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = grid.price,
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