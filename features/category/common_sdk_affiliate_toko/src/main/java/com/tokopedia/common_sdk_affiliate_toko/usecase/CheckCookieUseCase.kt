package com.tokopedia.common_sdk_affiliate_toko.usecase

import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieParams
import com.tokopedia.common_sdk_affiliate_toko.model.AffiliateCookieResponse
import com.tokopedia.common_sdk_affiliate_toko.model.CheckAffiliateCookieRequest
import com.tokopedia.common_sdk_affiliate_toko.model.toCheckCookieAdditionParam
import com.tokopedia.common_sdk_affiliate_toko.raw.GQL_Check_Cookie
import com.tokopedia.common_sdk_affiliate_toko.repository.CommonAffiliateRepository
import javax.inject.Inject

class CheckCookieUseCase @Inject constructor(
    private val commonAffiliateRepository: CommonAffiliateRepository
) {
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
                    param.affiliatePageDetail.source.getType(),
                    param.affiliatePageDetail.siteId,
                    param.affiliatePageDetail.verticalId
                ),
            )
        )
    }

   internal suspend fun checkAffiliateCookie(
        param: AffiliateCookieParams
    ): AffiliateCookieResponse {
        return commonAffiliateRepository.getGQLData(
            GQL_Check_Cookie,
            AffiliateCookieResponse::class.java,
            createRequestParam(param)
        )
    }
}