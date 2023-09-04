package com.tokopedia.search.result.mps.shopwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.analytics.SearchComponentTracking
import com.tokopedia.discovery.common.analytics.searchComponentTracking
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.result.mps.MPSTypeFactory
import com.tokopedia.search.result.mps.domain.model.MPSModel.SearchShopMPS.Shop
import com.tokopedia.search.utils.ComparableId

class MPSShopWidgetDataView(
    override val id: String = "",
    val name: String = "",
    val imageUrl: String = "",
    val city: String = "",
    val location: String = "",
    val subtitle: String = "",
    val applink: String = "",
    val componentId: String = "",
    val trackingOption: Int = 0,
    val keywords: String = "",
    val ticker: MPSShopTickerDataView = MPSShopTickerDataView(),
    val badgeList: List<MPSShopBadgeDataView> = listOf(),
    val shopFreeOngkir: MPSShopFreeOngkirDataView = MPSShopFreeOngkirDataView(),
    val buttonList: List<MPSButtonDataView> = listOf(),
    val productList: List<MPSShopWidgetProductDataView> = listOf(),
    val viewAllCard: MPSShopWidgetViewAllCardDataView = MPSShopWidgetViewAllCardDataView(),
): ImpressHolder(),
    Visitable<MPSTypeFactory>,
    ComparableId,
    SearchComponentTracking by searchComponentTracking(
        trackingOption = trackingOption,
        keyword = keywords,
        componentId = componentId,
        valueId = id,
        valueName = name,
        applink = applink,
    ) {

    fun willShowFreeOngkir() =
        shopFreeOngkir.isActive && shopFreeOngkir.imageUrl.isNotEmpty()

    fun willShowLocationFreeOngkirSeparator() =
        (location.isNotEmpty() || subtitle.isNotEmpty()) && willShowFreeOngkir()

    companion object {
        fun create(shopItem: Shop, keywords: String) = MPSShopWidgetDataView(
            id = shopItem.id,
            name = shopItem.name,
            imageUrl = shopItem.imageUrl,
            city = shopItem.city,
            location = shopItem.location,
            subtitle = shopItem.subtitle,
            applink = shopItem.applink,
            componentId = shopItem.componentId,
            trackingOption = shopItem.trackingOption,
            keywords = keywords,
            ticker = MPSShopTickerDataView.create(shopItem.ticker),
            badgeList = shopItem.badgeList.map(MPSShopBadgeDataView::create),
            shopFreeOngkir = MPSShopFreeOngkirDataView.create(shopItem.freeOngkir),
            buttonList = shopItem.buttonList
                .map { MPSButtonDataView.create(it, keywords, shopItem.id, shopItem.name) },
            productList = shopItem.productCardList.mapIndexed { index, product ->
                MPSShopWidgetProductDataView.create(
                    shop = shopItem,
                    product = product,
                    keywords = keywords,
                    position = index + 1,
                )
            },
            viewAllCard = MPSShopWidgetViewAllCardDataView.create(shopItem.viewAllCard, keywords)
        )
    }

    override fun type(typeFactory: MPSTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
