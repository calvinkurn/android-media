package com.tokopedia.shopwidget.shopcard

interface ShopCardListener {

    fun onItemImpressed()

    fun onItemClicked()

    fun onProductItemImpressed(productPreviewIndex: Int)

    fun onProductItemClicked(productPreviewIndex: Int)
}