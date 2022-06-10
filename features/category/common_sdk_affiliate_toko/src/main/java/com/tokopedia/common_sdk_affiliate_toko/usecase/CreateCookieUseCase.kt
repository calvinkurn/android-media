package com.tokopedia.common_sdk_affiliate_toko.usecase

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageType
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieRequest
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieResponse
import com.tokopedia.common_sdk_affiliate_toko.raw.GQL_Create_Cookie
import com.tokopedia.common_sdk_affiliate_toko.repository.CommonAffiliateRepository
import com.tokopedia.track.TrackApp

class CreateCookieUseCase {
    companion object {
        const val INPUT_PARAM = "input"
    }

    private fun createRequestParam(
        param: AffiliateCookieParams,
        deviceId: String
    ): HashMap<String, Any> {
        return hashMapOf(
            INPUT_PARAM to affiliateCookieDTO(param, deviceId)
        )
    }

    private fun affiliateCookieDTO(
        param: AffiliateCookieParams,
        deviceId: String
    ): CreateAffiliateCookieRequest {
        return CreateAffiliateCookieRequest(
            param.additionalParam,
            CreateAffiliateCookieRequest.AffiliateDetail(param.affiliateUUID),
            CreateAffiliateCookieRequest.CookieLevel(if (param.affiliatePageDetail.pageType == AffiliateSdkPageType.PDP) "Product" else "Page"),
            CreateAffiliateCookieRequest.Header(
                TrackApp.getInstance().gtm.irisSessionId,
                param.uuid,
                deviceId
            ),
            CreateAffiliateCookieRequest.LinkDetail(
                channel = param.affiliateChannel,
                affiliateLink = "",
                linkIdentifier = "",
                linkType = ""
            ),
            CreateAffiliateCookieRequest.PageDetail(
                param.affiliatePageDetail.pageId,
                param.affiliatePageDetail.pageType.ordinal.toString(),
                param.affiliatePageDetail.siteId,
                param.affiliatePageDetail.verticalId
            ),
            CreateAffiliateCookieRequest.PlatformDetail(
                platform = "android",
                ""
            ),
            CreateAffiliateCookieRequest.ProductDetail(
                param.productInfo.CategoryID,
                param.productInfo.IsVariant,
                param.productInfo.StockQty,
            ),
            CreateAffiliateCookieRequest.ShopDetail(param.productInfo.shopId)
        )
    }

    suspend fun createCookieRequest(
        param: AffiliateCookieParams,
        deviceId: String
    ): CreateAffiliateCookieResponse {
        return CommonAffiliateRepository.getGQLData(
            GQL_Create_Cookie,
            CreateAffiliateCookieResponse::class.java,
            createRequestParam(param, deviceId)

        )
    }
}