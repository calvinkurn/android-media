package com.tokopedia.favorite.view.viewlistener

import com.tokopedia.favorite.view.viewmodel.TopAdsShopItem

interface TopAdsResourceListener {

    fun onTopAdsResourceReady(className: String, shopItem: TopAdsShopItem)

}
