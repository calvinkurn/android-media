package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.ShopViewModel

interface ShopListener {

    fun onItemClicked(shopItem: ShopViewModel.ShopItem)

    fun onProductItemClicked(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct)
}