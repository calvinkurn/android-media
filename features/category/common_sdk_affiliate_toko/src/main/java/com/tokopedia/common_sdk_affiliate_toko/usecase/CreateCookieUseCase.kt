package com.tokopedia.common_sdk_affiliate_toko.usecase

import com.tokopedia.common_sdk_affiliate_toko.model.*
import com.tokopedia.common_sdk_affiliate_toko.raw.GQL_Create_Cookie
import com.tokopedia.common_sdk_affiliate_toko.repository.CommonAffiliateRepository
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class CreateCookieUseCase @Inject constructor(
    private val commonAffiliateRepository: CommonAffiliateRepository
){
    companion object {
        const val INPUT_PARAM = "input"
    }

    private fun createRequestParam(
        param: AffiliateCookieParams,
        deviceId: String,
        androidVersion: String
    ): HashMap<String, Any> {
        return hashMapOf(
            INPUT_PARAM to affiliateCookieDTO(param, deviceId, androidVersion)
        )
    }

    private fun affiliateCookieDTO(
        param: AffiliateCookieParams,
        deviceId: String,
        androidVersion: String
    ): CreateAffiliateCookieRequest {
        return CreateAffiliateCookieRequest(
            param.toCreateCookieAdditionParam(),
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
                androidVersion
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
        deviceId: String,
        androidVersion:String
    ): CreateAffiliateCookieResponse {
        return commonAffiliateRepository.getGQLData(
            GQL_Create_Cookie,
            CreateAffiliateCookieResponse::class.java,
            createRequestParam(param, deviceId, androidVersion)

        )
    }
}