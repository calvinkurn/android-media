package com.tokopedia.shopwidget

interface ShopWidgetListener {
    fun onShopCardClicked(shopCardModel: ShopWidgetModel.ShopCardModel)

    fun onShopSeeMoreClicked(shopCardModel: ShopWidgetModel.ShopCardModel)
}