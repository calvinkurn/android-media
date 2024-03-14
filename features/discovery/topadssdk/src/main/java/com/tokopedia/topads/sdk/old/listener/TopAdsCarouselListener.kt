package com.tokopedia.topads.sdk.old.listener


interface TopAdsCarouselListener {

    fun onItemImpressed(position: Int)

    fun onItemClicked(position: Int)

    fun onProductItemClicked(productIndex: Int, shopIndex: Int)
}

