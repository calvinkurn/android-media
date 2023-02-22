package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.mps.MPSTypeFactory
import com.tokopedia.search.result.mps.domain.model.MPSModel.AceSearchShop.Shop

class MPSShopWidgetDataView(
    val shopId: String = "",
    val shopName: String = "",
    val shopImage: String = "",
    val shopLocation: String = "",
    val shopButtonTitle: String = "",
    val shopBadge: MPSShopBadgeDataView = MPSShopBadgeDataView(),
    val shopFreeOngkir: MPSShopFreeOngkirDataView = MPSShopFreeOngkirDataView(),
    val productList: List<MPSShopWidgetProductDataView> = listOf(),
): Visitable<MPSTypeFactory> {

    fun willShowFreeOngkir() =
        shopFreeOngkir.isActive && shopFreeOngkir.imageUrl.isNotEmpty()

    fun willShowLocationFreeOngkirSeparator() =
        shopLocation.isNotEmpty() && willShowFreeOngkir()

    companion object {
        fun create(shopItem: Shop) = MPSShopWidgetDataView(
            shopId = shopItem.id,
            shopName = shopItem.name,
            shopImage = shopItem.image,
            shopLocation = shopItem.location,
            shopButtonTitle = shopItem.buttonTitle,
            shopBadge = MPSShopBadgeDataView.create(shopItem.badge),
            shopFreeOngkir = MPSShopFreeOngkirDataView.create(shopItem.freeOngkir),
            productList = shopItem.productList.map(MPSShopWidgetProductDataView::create)
        )
    }

    override fun type(typeFactory: MPSTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
