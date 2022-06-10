package com.tokopedia.common_sdk_affiliate_toko.usecase

import com.tokopedia.common_sdk_affiliate_toko.model.*
import com.tokopedia.common_sdk_affiliate_toko.raw.GQL_Check_Cookie
import com.tokopedia.common_sdk_affiliate_toko.repository.CommonAffiliateRepository

class CheckCookieUseCase {
    companion object {
        const val INPUT_PARAM = "input"
    }

    private fun createRequestParam(
        param: AffiliateCookieParams
    ): HashMap<String, Any> {
        return hashMapOf(
            INPUT_PARAM to CheckAffiliateCookieRequest(
                param.toCheckCookieAdditionParam(),
                CheckAffiliateCookieRequest.PageDetail(
                    param.affiliatePageDetail.pageId,
                    param.affiliatePageDetail.pageType.ordinal.toString(),
                    param.affiliatePageDetail.siteId,
                    param.affiliatePageDetail.verticalId
                ),
            )
        )
    }

    suspend fun checkAffiliateCookie(
        param: AffiliateCookieParams
    ): AffiliateCookieResponse {
        return CommonAffiliateRepository.getGQLData(
            GQL_Check_Cookie,
            AffiliateCookieResponse::class.java,
            createRequestParam(param)
        )
    }
}