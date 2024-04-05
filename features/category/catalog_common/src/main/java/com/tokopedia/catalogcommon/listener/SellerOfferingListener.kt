package com.tokopedia.catalogcommon.listener

interface SellerOfferingListener {
    fun onSellerOfferingAtcButtonClicked(productId: String, shopId: String)
    fun onSellerOfferingChatButtonClicked()
    fun onSellerOfferingProductImageClicked(productId: String)
    fun onSellerOfferingVariantArrowClicked(productId: String, shopId: String)
    fun onSellerOfferingProductInfo(productId: String, shopId: String)

    fun onImpressionSellerOffering(productId: String, shopId: String)
}
