package com.tokopedia.common_sdk_affiliate_toko.usecase

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateSdkPageSource
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieRequest
import com.tokopedia.common_sdk_affiliate_toko.model.CreateAffiliateCookieResponse
import com.tokopedia.common_sdk_affiliate_toko.model.toCreateCookieAdditionParam
import com.tokopedia.common_sdk_affiliate_toko.raw.GQL_Create_Cookie
import com.tokopedia.common_sdk_affiliate_toko.repository.CommonAffiliateRepository
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateSdkConstant
import com.tokopedia.config.GlobalConfig
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
            param.toCreateCookieAdditionParam(),
            CreateAffiliateCookieRequest.AffiliateDetail(param.affiliateUUID),
            CreateAffiliateCookieRequest.CookieLevel(
                if (param.affiliatePageDetail.source is AffiliateSdkPageSource.PDP) {
                    AffiliateSdkConstant.PRODUCT
                } else {
                    AffiliateSdkConstant.PAGE
                }
            ),
            CreateAffiliateCookieRequest.Header(
                TrackApp.getInstance().gtm.irisSessionId,
                param.uuid,
                deviceId,
                param.affiliatePageDetail.source.atcSource.source
            ),
            CreateAffiliateCookieRequest.LinkDetail(
                channel = param.affiliateChannel
            ),
            CreateAffiliateCookieRequest.PageDetail(
                param.affiliatePageDetail.pageId,
                param.affiliatePageDetail.source.getType(),
                param.affiliatePageDetail.siteId,
                param.affiliatePageDetail.verticalId
            ),
            CreateAffiliateCookieRequest.PlatformDetail(
                platform = AffiliateSdkConstant.PLATFORM,
                GlobalConfig.VERSION_NAME
            ),
            CreateAffiliateCookieRequest.ProductDetail(
                param.affiliatePageDetail.source.productInfo.categoryID,
                param.affiliatePageDetail.source.productInfo.isVariant,
                param.affiliatePageDetail.source.productInfo.stockQty
            ),
            CreateAffiliateCookieRequest.ShopDetail(param.affiliatePageDetail.source.shopId)
        )
    }

    internal suspend fun createCookieRequest(
        param: AffiliateCookieParams,
        deviceId: String
    ): CreateAffiliateCookieResponse {
        return commonAffiliateRepository.getGQLData(
            GQL_Create_Cookie,
            CreateAffiliateCookieResponse::class.java,
            createRequestParam(param, deviceId)
        )
    }
}
