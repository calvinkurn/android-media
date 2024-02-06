package com.tokopedia.catalogcommon.listener

interface SellerOfferingListener {
    fun onSellerOfferingAtcButtonClicked()
    fun onSellerOfferingChatButtonClicked()
    fun onSellerOfferingProductImageClicked(productId: String)
    fun onSellerOfferingVariantArrowClicked(productId: String)
}
