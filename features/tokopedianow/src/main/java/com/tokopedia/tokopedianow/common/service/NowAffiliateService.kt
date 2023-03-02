package com.tokopedia.tokopedianow.common.service

import com.tokopedia.applink.ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.model.NowAffiliateData
import java.util.UUID
import javax.inject.Inject

class NowAffiliateService @Inject constructor(
    private val affiliateCookieHelper: AffiliateCookieHelper
) {

    private val shopId = TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2
    private var affiliateData = NowAffiliateData()

    suspend fun initAffiliateCookie(affiliateUuid: String = "", affiliateChannel: String = "") {
        val source = AffiliateSdkPageSource.Shop(shopId)
        val pageDetail = AffiliatePageDetail(shopId, source)

        affiliateData = NowAffiliateData(
            affiliateUuid = affiliateUuid,
            affiliateChannel = affiliateChannel,
            affiliateTrackerId = UUID.randomUUID().toString()
        )

        affiliateCookieHelper.initCookie(
            affiliateUUID = affiliateUuid,
            affiliateChannel = affiliateChannel,
            affiliatePageDetail = pageDetail
        )
    }

    suspend fun checkAtcAffiliateCookie(data: NowAffiliateAtcData) {
        val productId = data.productId
        val stock = data.stock
        val isVariant = data.isVariant
        val newQuantity = data.newQuantity
        val currentQuantity = data.currentQuantity

        if(newQuantity > currentQuantity) {
            createAtcAffiliateCookie(productId, stock, isVariant)
        }
    }

    private suspend fun createAtcAffiliateCookie(
        productId: String,
        stockQty: Int,
        isVariant: Boolean
    ) {
        val productInfo = AffiliateSdkProductInfo(
            categoryID = "",
            isVariant = isVariant,
            stockQty = stockQty
        )

        val source = AffiliateSdkPageSource.DirectATC(
            atcSource = AffiliateAtcSource.SHOP_PAGE,
            shopId = shopId,
            productInfo = productInfo
        )

        affiliateCookieHelper.initCookie(
            affiliateData.affiliateUuid,
            affiliateData.affiliateChannel,
            AffiliatePageDetail(productId, source)
        )
    }

    fun createAffiliateLink(url: String): String {
        val trackerId = affiliateData.affiliateTrackerId
        return affiliateCookieHelper.createAffiliateLink(url, trackerId)
    }
}
