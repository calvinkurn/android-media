package com.tokopedia.topads.sdk.v2.dynamicfeedshop.listener

import com.tokopedia.topads.sdk.domain.model.Data

interface LocalAdsClickListener {
    fun onShopItemClicked(position: Int, data: Data?)
    fun onProductItemClicked(position: Int, data: Data?)
    fun onAddFavorite(position: Int, dataShop: Data?)
}
