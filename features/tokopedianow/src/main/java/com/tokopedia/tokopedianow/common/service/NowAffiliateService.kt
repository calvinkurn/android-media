package com.tokopedia.tokopedianow.common.service

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.model.NowAffiliateData
import com.tokopedia.tokopedianow.common.util.ShopIdProvider.getShopId
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.model.AffiliatePDPInput
import com.tokopedia.universal_sharing.view.model.PageDetail
import com.tokopedia.universal_sharing.view.model.Product
import com.tokopedia.universal_sharing.view.model.Shop
import java.util.UUID
import javax.inject.Inject

class NowAffiliateService @Inject constructor(
    private val affiliateCookieHelper: AffiliateCookieHelper
) {

    companion object {
        private const val SHARE_PAGE_TYPE = "shop"
        private const val SHARE_SITE_ID = "1"
        private const val SHARE_VERTICAL_ID = "1"
        private const val SHOP_STATUS_OPEN = 1
    }

    private var affiliateData = NowAffiliateData()

    suspend fun initAffiliateCookie(affiliateUuid: String = "", affiliateChannel: String = "") {
        val source = AffiliateSdkPageSource.Shop(getShopId())
        val pageDetail = AffiliatePageDetail(getShopId(), source)

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
            shopId = getShopId(),
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

    fun createShareInput(): AffiliatePDPInput {
        val pageDetail = PageDetail(
            pageId = getShopId(),
            pageType = SHARE_PAGE_TYPE,
            siteId = SHARE_SITE_ID,
            verticalId = SHARE_VERTICAL_ID
        )

        val shop = Shop(
            shopID = getShopId(),
            shopStatus = SHOP_STATUS_OPEN,
            isOS = true,
            isPM = false
        )

        return AffiliatePDPInput(
            pageDetail = pageDetail,
            pageType = PageType.SHOP.value,
            product = Product(),
            shop = shop
        )
    }
}
