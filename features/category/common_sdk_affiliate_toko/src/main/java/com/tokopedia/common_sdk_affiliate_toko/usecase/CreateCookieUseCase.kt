package com.tokopedia.common_sdk_affiliate_toko.usecase

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieRequest
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieResponse
import com.tokopedia.common_sdk_affiliate_toko.model.toCreateCookieAdditionParam
import com.tokopedia.common_sdk_affiliate_toko.raw.GQL_Create_Cookie
import com.tokopedia.common_sdk_affiliate_toko.repository.CommonAffiliateRepository
import com.tokopedia.track.TrackApp
import javax.inject.Inject

class CreateCookieUseCase @Inject constructor(
    private val commonAffiliateRepository: CommonAffiliateRepository
) {
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
            CreateAffiliateCookieRequest.CookieLevel(if (param.affiliatePageDetail.source is AffiliateSdkPageSource.PDP) "Product" else "Page"),
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
                param.affiliatePageDetail.source.getType(),
                param.affiliatePageDetail.siteId,
                param.affiliatePageDetail.verticalId
            ),
            CreateAffiliateCookieRequest.PlatformDetail(
                platform = "android",
                androidVersion
            ),
            CreateAffiliateCookieRequest.ProductDetail(
                param.affiliatePageDetail.source.productInfo.CategoryID,
                param.affiliatePageDetail.source.productInfo.IsVariant,
                param.affiliatePageDetail.source.productInfo.StockQty,
            ),
            CreateAffiliateCookieRequest.ShopDetail(param.affiliatePageDetail.source.shopId)
        )
    }

    internal suspend fun createCookieRequest(
        param: AffiliateCookieParams,
        deviceId: String,
        androidVersion: String
    ): CreateAffiliateCookieResponse {
        return commonAffiliateRepository.getGQLData(
            GQL_Create_Cookie,
            CreateAffiliateCookieResponse::class.java,
            createRequestParam(param, deviceId, androidVersion)

        )
    }
}