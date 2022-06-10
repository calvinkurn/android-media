package com.tokopedia.common_sdk_affiliate_toko.model


data class AffiliateCookieParams(
    val affiliatePageDetail: AffiliatePageDetail,
    val affiliateChannel: String,
    val uuid: String,
    val productInfo: AffiliateSdkProductInfo = AffiliateSdkProductInfo(),
    val affiliateUUID: String = ""
)

data class AffiliatePageDetail(
    val pageType: AffiliateSdkPageType,
    val pageId: String,
    val siteId: String = "1",
    val verticalId: String = "1",
)

enum class AffiliateSdkPageType {
    PDP,
    SHOP,
    CLP,
    CAMPAIGN,
    CATALOG
}