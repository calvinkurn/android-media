package com.tokopedia.search.result.presentation.view.listener


import com.tokopedia.search.result.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.model.ShopViewModelKt

interface ShopListener {

    fun onItemClicked(shopItem: ShopViewModel.ShopViewItem, adapterPosition: Int)

    fun onItemClicked(shopItem: ShopViewModelKt.ShopItem)

    fun onProductItemClicked(applink: String)

    fun onFavoriteButtonClicked(shopItem: ShopViewModel.ShopViewItem, adapterPosition: Int)
}