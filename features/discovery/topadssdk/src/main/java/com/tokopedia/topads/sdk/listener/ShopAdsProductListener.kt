package com.tokopedia.topads.sdk.listener


interface ShopAdsProductListener {

    fun onItemImpressed(position: Int)

    fun onItemClicked(position: Int)
}
