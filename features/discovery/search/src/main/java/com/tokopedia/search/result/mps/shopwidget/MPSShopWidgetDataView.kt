package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.mps.MPSTypeFactory
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop

class MPSShopWidgetDataView(
    val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val applink: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val ticker: MPSShopTickerDataView = MPSShopTickerDataView(),
    val badgeList: List<MPSShopBadgeDataView> = listOf(),
    val shopFreeOngkir: MPSShopFreeOngkirDataView = MPSShopFreeOngkirDataView(),
    val buttonList: List<MPSButtonDataView> = listOf(),
    val productList: List<MPSShopWidgetProductDataView> = listOf(),
): Visitable<MPSTypeFactory> {

    fun willShowFreeOngkir() =
        shopFreeOngkir.isActive && shopFreeOngkir.imageUrl.isNotEmpty()

    fun willShowLocationFreeOngkirSeparator() =
        location.isNotEmpty() && willShowFreeOngkir()

    companion object {
        fun create(shopItem: Shop) = MPSShopWidgetDataView(
            id = shopItem.id,
            name = shopItem.name,
            imageUrl = shopItem.imageUrl,
            location = shopItem.location,
            applink = shopItem.applink,
            componentId = shopItem.componentId,
            trackingOption = shopItem.trackingOption,
            ticker = MPSShopTickerDataView.create(shopItem.ticker),
            badgeList = shopItem.badgeList.map(MPSShopBadgeDataView::create),
            shopFreeOngkir = MPSShopFreeOngkirDataView.create(shopItem.freeOngkir),
            buttonList = shopItem.buttonList.map(MPSButtonDataView::create),
            productList = shopItem.productList.map(MPSShopWidgetProductDataView::create),
        )
    }

    override fun type(typeFactory: MPSTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
