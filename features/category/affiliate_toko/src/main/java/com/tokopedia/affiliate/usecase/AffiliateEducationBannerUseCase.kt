package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Education_Banner
import com.tokopedia.affiliate.model.response.AffiliateEducationBannerResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateEducationBannerUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[SOURCE_KEY] = SOURCE
        request[IS_HOME_BANNER_KEY] = IS_HOME_BANNER
        return request
    }

    suspend fun getEducationBanners(): AffiliateEducationBannerResponse {
        return repository.getGQLData(
            GQL_Affiliate_Education_Banner,
            AffiliateEducationBannerResponse::class.java,
            createRequestParams()
        )
    }

    companion object {
        private const val SOURCE_KEY = "source"
        private const val IS_HOME_BANNER_KEY = "is_home_banner"
        private const val SOURCE = "affiliate"
        private const val IS_HOME_BANNER = 1
    }
}
