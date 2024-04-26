package com.tokopedia.topads.sdk.v2.shopadslayout6.listener


interface TopAdsCarouselListener {

    fun onItemImpressed(position: Int)

    fun onItemClicked(position: Int)

    fun onProductItemClicked(productIndex: Int, shopIndex: Int)
}

