package com.tokopedia.topads.sdk.old.listener

import com.tokopedia.topads.sdk.old.domain.model.Data

interface LocalAdsClickListener {
    fun onShopItemClicked(position: Int, data: Data?)
    fun onProductItemClicked(position: Int, data: Data?)
    fun onAddFavorite(position: Int, dataShop: Data?)
}
