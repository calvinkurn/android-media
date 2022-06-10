package com.tokopedia.common_sdk_affiliate_toko.model

import com.google.gson.annotations.SerializedName


class AffiliateCookieParams(
    val affiliatePageDetail: AffiliatePageDetail,
    val affiliateChannel: String,
    val uuid: String,
    val additionalParam: List<CreateAffiliateCookieRequest.AdditionalParam>,
    val productInfo: AffiliateSdkProductInfo = AffiliateSdkProductInfo(),
    val affiliateUUID: String = ""
)

class AffiliatePageDetail(
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