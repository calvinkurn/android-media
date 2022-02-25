package com.tokopedia.topads.sdk.listener


interface TopAdsCarouselListener {

    fun onItemImpressed(position: Int)

    fun onItemClicked(position: Int)

    fun onProductItemClicked(productIndex: Int, shopIndex: Int)
}

