package com.tokopedia.common_sdk_affiliate_toko.model


internal class AffiliateCookieParams(
    val affiliateUUID: String,
    val affiliateChannel: String,
    val affiliatePageDetail: AffiliatePageDetail,
    val uuid: String = "",
    val additionalParam: List<AdditionalParam> = emptyList(),
)

 class AdditionalParam(
    var key: String?,
    var value: String?
)

 class AffiliatePageDetail(
    val pageId: String,
    val source: AffiliateSdkPageSource,
    val siteId: String = "1",
    val verticalId: String = "1",
)

sealed class AffiliateSdkPageSource(
    internal val shopId: String,
    internal val productInfo: AffiliateSdkProductInfo = AffiliateSdkProductInfo()
) {
    internal open fun getType() = ""
    class PDP(shopId: String, productInfo: AffiliateSdkProductInfo) :
        AffiliateSdkPageSource(shopId, productInfo) {
        override fun getType() = "pdp"
    }

    class Shop(shopId: String) :
        AffiliateSdkPageSource(shopId) {
        override fun getType() = "shop"

    }
}

internal fun AffiliateCookieParams.toCreateCookieAdditionParam(): List<CreateAffiliateCookieRequest.AdditionalParam> {
    return additionalParam.map { CreateAffiliateCookieRequest.AdditionalParam(it.key, it.value) }
}

internal fun AffiliateCookieParams.toCheckCookieAdditionParam(): List<CheckAffiliateCookieRequest.AdditionalParam> {
    return additionalParam.map { CheckAffiliateCookieRequest.AdditionalParam(it.key, it.value) }
}