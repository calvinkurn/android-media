package com.tokopedia.recommendation_widget_common.affiliate

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliatePageDetail
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateAtcSource
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import dagger.Lazy
import javax.inject.Inject

class RecommendationNowAffiliate @Inject constructor(
    private val affiliateCookieHelper: Lazy<AffiliateCookieHelper>,
) {

    suspend fun initCookie(affiliateTrackerId: String) {
        val shopId = getNowShopId()
        affiliateCookieHelper.get().initCookie(
            "",
            "",
            AffiliatePageDetail(
                pageId = shopId,
                source = AffiliateSdkPageSource.Shop(shopId),
            ),
            affiliateTrackerId,
        )
    }

    private fun getNowShopId(): String =
        if (GlobalConfig.DEBUG) ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_1
        else ApplinkConst.TokopediaNow.TOKOPEDIA_NOW_PRODUCTION_SHOP_ID_2

    suspend fun initCookieDirectATC(
        recommendationNowAffiliateData: RecommendationNowAffiliateData = RecommendationNowAffiliateData(),
        recommendationItem: RecommendationItem,
    ) {
        affiliateCookieHelper.get().initCookie(
            affiliateUUID = recommendationNowAffiliateData.affiliateUniqueId,
            affiliateChannel = recommendationNowAffiliateData.affiliateChannel,
            affiliatePageDetail = AffiliatePageDetail(
                pageId = recommendationItem.productId.toString(),
                source = AffiliateSdkPageSource.DirectATC(
                    atcSource = AffiliateAtcSource.SHOP_PAGE,
                    shopId = getNowShopId(),
                    productInfo = recommendationItem.toAffiliateSdkProductInfo(),
                )
            ),
            uuid = recommendationNowAffiliateData.affiliateTrackerId,
        )
    }
}
