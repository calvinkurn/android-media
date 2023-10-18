package com.tokopedia.tokopedianow.common.service

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkProductInfo
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.kotlin.extensions.view.decodeToUtf8
import com.tokopedia.tokopedianow.common.model.NowAffiliateAtcData
import com.tokopedia.tokopedianow.common.model.NowAffiliateData
import com.tokopedia.tokopedianow.common.util.ShopIdProvider
import com.tokopedia.universal_sharing.tracker.PageType
import com.tokopedia.universal_sharing.view.model.AffiliateInput
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
        val uuid = affiliateUuid.decodeToUtf8()
        val shopId = ShopIdProvider.getShopId()
        val source = AffiliateSdkPageSource.Shop(shopId)
        val pageDetail = AffiliatePageDetail(shopId, source)

        affiliateData = NowAffiliateData(
            affiliateUuid = uuid,
            affiliateChannel = affiliateChannel,
            affiliateTrackerId = UUID.randomUUID().toString()
        )

        affiliateCookieHelper.initCookie(
            affiliateUUID = uuid,
            affiliateChannel = affiliateChannel,
            affiliatePageDetail = pageDetail
        )
    }

    suspend fun checkAtcAffiliateCookie(data: NowAffiliateAtcData) {
        val newQuantity = data.newQuantity
        val currentQuantity = data.currentQuantity

        if (newQuantity > currentQuantity) {
            val productId = data.productId
            val shopId = data.shopId
            val stock = data.stock
            val isVariant = data.isVariant

            createAtcAffiliateCookie(productId, shopId, stock, isVariant)
        }
    }

    private suspend fun createAtcAffiliateCookie(
        productId: String,
        shopId: String,
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

    fun createShareInput(): AffiliateInput {
        val shopId = ShopIdProvider.getShopId()
        val pageDetail = PageDetail(
            pageId = shopId,
            pageType = SHARE_PAGE_TYPE,
            siteId = SHARE_SITE_ID,
            verticalId = SHARE_VERTICAL_ID
        )

        val shop = Shop(
            shopID = shopId,
            shopStatus = SHOP_STATUS_OPEN,
            isOS = true,
            isPM = false
        )

        return AffiliateInput(
            pageDetail = pageDetail,
            pageType = PageType.SHOP.value,
            product = Product(),
            shop = shop
        )
    }
}
