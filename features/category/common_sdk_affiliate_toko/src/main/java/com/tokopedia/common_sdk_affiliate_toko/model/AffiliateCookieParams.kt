package com.tokopedia.common_sdk_affiliate_toko.model

import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateSdkConstant

internal class AffiliateCookieParams(
    val affiliateUUID: String,
    val affiliateChannel: String,
    val affiliatePageDetail: AffiliatePageDetail,
    val uuid: String = "",
    val additionalParam: List<AdditionalParam> = emptyList()
)

/**
 * Creates a String Key-Value type additional param
 *
 * @param[key] String key
 * @param[value] String value
 */
class AdditionalParam(
    var key: String?,
    var value: String?
)

/**
 * Encapsulates all the page details
 *
 * @param[pageId] productId for Product, shopId for Shop and campaignId for campaign
 * @param[source] [AffiliateSdkPageSource]
 * @param[siteId] if not provided default value is always 1
 * @param[verticalId] if not provided default value is always 1
 */
class AffiliatePageDetail(
    val pageId: String,
    val source: AffiliateSdkPageSource,
    val siteId: String = "1",
    val verticalId: String = "1"
)

/**
 * helper class for page source i.e. PDP, Shop, Campaign etc.
 *
 * @see PDP
 * @see Shop
 */
sealed class AffiliateSdkPageSource(
    internal val shopId: String,
    internal val atcSource: AffiliateAtcSource = AffiliateAtcSource.PDP,
    internal val productInfo: AffiliateSdkProductInfo = AffiliateSdkProductInfo("", false, 0)
) {
    internal open fun getType() = ""
    internal open fun shouldCallCheckCookie() = false

    /**
     * Encapsulates info for PDP page source.
     *
     * @param[shopId] shopId of product
     * @param[productInfo] [AffiliateSdkProductInfo]
     */
    class PDP(shopId: String, productInfo: AffiliateSdkProductInfo) :
        AffiliateSdkPageSource(shopId, productInfo = productInfo) {
        override fun getType() = AffiliateSdkConstant.PDP
    }

    /**
     * Encapsulates info for Shop page source.
     *
     * @param[shopId] shopId of Shop
     */
    class Shop(shopId: String = "") :
        AffiliateSdkPageSource(shopId) {
        override fun getType() = AffiliateSdkConstant.SHOP
        override fun shouldCallCheckCookie() = true
    }

    /**
     * Encapsulates info for DirectATC.
     *
     * @param[atcSource] [AffiliateAtcSource]
     * @param[productInfo] [AffiliateSdkProductInfo]
     */
    class DirectATC(
        atcSource: AffiliateAtcSource,
        productInfo: AffiliateSdkProductInfo?
    ) :
        AffiliateSdkPageSource(
            "",
            atcSource,
            productInfo = productInfo ?: AffiliateSdkProductInfo("", false, 0)
        ) {
        override fun getType() = AffiliateSdkConstant.PDP
    }
}

internal fun AffiliateCookieParams.toCreateCookieAdditionParam(): List<CreateAffiliateCookieRequest.AdditionalParam> {
    return additionalParam.map { CreateAffiliateCookieRequest.AdditionalParam(it.key, it.value) }
}

internal fun AffiliateCookieParams.toCheckCookieAdditionParam(): List<CheckAffiliateCookieRequest.AdditionalParam> {
    return additionalParam.map { CheckAffiliateCookieRequest.AdditionalParam(it.key, it.value) }
}
