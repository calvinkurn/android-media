package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliatePerformanceData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliatePerformanceUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(page: Int, limit: Int): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_PAGE] = page
        request[PARAM_LIMIT] = limit
        return request
    }

    suspend fun affiliatePerformance(page: Int, limit : Int): AffiliatePerformanceData {
        return repository.getGQLData(
                GQL_Affiliate_Performance,
                AffiliatePerformanceData::class.java,
                createRequestParams(page,limit)
        )
    }


    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_LIMIT = "limit"
    }
}