package com.tokopedia.search.result.mps.shopwidget

interface MPSShopWidgetListener {

    fun onShopImpressed(mpsShopWidgetDataView: MPSShopWidgetDataView)

    fun onSeeShopClicked(mpsShopWidgetDataView: MPSShopWidgetDataView)

    fun onSeeAllCardClicked(mpsShopWidgetDataView: MPSShopWidgetDataView)

    fun onProductItemImpressed(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?,
    )

    fun onProductItemClicked(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?,
    )

    fun onProductItemAddToCart(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    )

    fun onProductItemSeeOtherProductClick(
        mpsShopWidgetDataView: MPSShopWidgetDataView,
        mpsShopWidgetProductDataView: MPSShopWidgetProductDataView?
    )
}
