package com.tokopedia.shop.common.view.interfaces

interface ShopPageSharedListener {
    fun createPdpAffiliateLink(basePdpAppLink: String): String
    fun createAffiliateCookieAtcProduct(
        productId: String,
        isVariant: Boolean,
        stockQty: Int
    )
}
