package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.Data
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.domain.model.Shop

interface TopAdsItemClickListener {
    fun onProductItemClicked(position: Int, product: Product)
    fun onShopItemClicked(position: Int, shop: Shop)
    fun onAddFavorite(position: Int, data: Data)
}
