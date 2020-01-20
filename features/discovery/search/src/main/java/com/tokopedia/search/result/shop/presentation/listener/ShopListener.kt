package com.tokopedia.search.result.shop.presentation.listener

import com.tokopedia.search.result.shop.presentation.model.ShopViewModel

internal interface ShopListener {

    fun onItemClicked(shopItem: ShopViewModel.ShopItem)

    fun onProductItemClicked(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct)
}