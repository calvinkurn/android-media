package com.tokopedia.topads.sdk.old.listener


interface ShopAdsProductListener {

    fun onItemImpressed(position: Int)

    fun onItemClicked(position: Int)
}
