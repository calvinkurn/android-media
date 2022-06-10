package com.tokopedia.common_sdk_affiliate_toko.model

import com.google.gson.annotations.SerializedName


class AffiliateCookieParams(
    val affiliatePageDetail: AffiliatePageDetail,
    val affiliateChannel: String,
    val uuid: String,
    val additionalParam: List<AdditionalParam>,
    val productInfo: AffiliateSdkProductInfo = AffiliateSdkProductInfo(),
    val affiliateUUID: String = ""
)

class AdditionalParam(
    var key: String?,
    var value: String?
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

fun AffiliateCookieParams.toCreateCookieAdditionParam(): List<CreateAffiliateCookieRequest.AdditionalParam> {
    return additionalParam.map { CreateAffiliateCookieRequest.AdditionalParam(it.key, it.value) }
}

fun AffiliateCookieParams.toCheckCookieAdditionParam(): List<CheckAffiliateCookieRequest.AdditionalParam> {
    return additionalParam.map { CheckAffiliateCookieRequest.AdditionalParam(it.key, it.value) }
}