package com.tokopedia.home.analytics.v2


import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.home.analytics.v2.BaseTracking.Value.LIST
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

object MixTopTracking : BaseTracking() {
    private class CustomAction{
        companion object {
            val IMPRESSION_ON_CAROUSEL_PRODUCT = Action.IMPRESSION_ON.format("list carousel product")
            val CLICK_ON_CAROUSEL_PRODUCT = Action.CLICK_ON.format("list carousel product")
            const val CLICK_VIEW_ALL_CAROUSEL_PRODUCT = "click view all on list carousel product"
        }
    }

    private class CustomActionField{
        companion object {
            val LIST_CAROUSEL_PRODUCT = LIST.format("%s", "list carousel product", "%s")
        }
    }

    fun getMixTopView(products: List<Product>, headerName: String, positionOnWidgetHome: String) = getBasicProductView(
            Event.PRODUCT_VIEW,
            Category.HOMEPAGE,
            CustomAction.IMPRESSION_ON_CAROUSEL_PRODUCT,
            Label.NONE,
            CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, headerName),
            products
    )

    fun getMixTopClick(products: List<Product>, headerName: String, channelId: String, positionOnWidgetHome: String) = getBasicProductChannelClick(
            Event.PRODUCT_CLICK,
            Category.HOMEPAGE,
            CustomAction.CLICK_ON_CAROUSEL_PRODUCT,
            headerName,
            CustomActionField.LIST_CAROUSEL_PRODUCT.format(positionOnWidgetHome, headerName),
            channelId,
            products
    )

    fun getMixTopSeeAllClick(headerName: String) = DataLayer.mapOf(
            Event.KEY, Event.CLICK_HOMEPAGE,
            Category.KEY, Category.HOMEPAGE,
            Action.KEY, CustomAction.CLICK_VIEW_ALL_CAROUSEL_PRODUCT,
            Label.KEY, headerName
    )

    private fun mapGridToProductTracker(grid: DynamicHomeChannel.Grid, channelId: String, position: Int) = Product(
            id = grid.id,
            name = grid.name,
            brand = "",
            category = "",
            channelId = channelId,
            isFreeOngkir = grid.freeOngkir.isActive,
            productPosition = position.toString(),
            productPrice = CurrencyFormatHelper.convertRupiahToInt(grid.price).toString(),
            variant = ""
    )

    fun mapChannelToProductTracker(channels: DynamicHomeChannel.Channels) = channels.grids.withIndex().map { mapGridToProductTracker(it.value, channels.id,  it.index) }

}