package com.tokopedia.common_sdk_affiliate_toko.model



sealed class AffiliateCookieParams {
     class PDP(
       val productInfo: AffiliateSdkProductInfo,
       val affiliateUUID: String = ""
     ) : AffiliateCookieParams()

    class  AffiliateSdkPage(
      val pageType: AffiliateSdkPageType,
      val productInfo: AffiliateSdkProductInfo,
      val pageId: String,
      val siteId: String = "1",
      val verticalId: String = "1",
      val affiliateUUID: String = ""
    ) : AffiliateCookieParams()

}
enum class AffiliateSdkPageType {
    PDP,
    SHOP,
    CLP,
    CAMPAIGN,
    CATALOG
}