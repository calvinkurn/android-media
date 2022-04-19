package com.tokopedia.search.result.shop.presentation.listener

import com.tokopedia.search.result.shop.presentation.model.ShopDataView

internal interface ShopListener {

    fun onItemClicked(shopDataItem: ShopDataView.ShopItem)

    fun onProductItemClicked(shopDataItemProduct: ShopDataView.ShopItem.ShopItemProduct)
}