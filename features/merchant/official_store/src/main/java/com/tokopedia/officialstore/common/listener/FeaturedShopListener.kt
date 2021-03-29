package com.tokopedia.officialstore.common.listener

import com.tokopedia.officialstore.official.data.model.Shop

interface FeaturedShopListener {
    fun onShopImpression(categoryName: String, position: Int, shopData: Shop)
    fun onShopClick(categoryName: String, position: Int, shopData: Shop)
}