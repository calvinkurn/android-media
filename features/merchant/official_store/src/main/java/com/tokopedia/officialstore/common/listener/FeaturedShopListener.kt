package com.tokopedia.officialstore.common.listener

import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid

interface FeaturedShopListener {
    fun onShopImpression(position: Int, shopData: Grid)
    fun onShopClick(position: Int, shopData: Grid)
}