package com.tokopedia.exploreCategory.usecase

import com.tokopedia.exploreCategory.model.AffiliatePerformanceData
import com.tokopedia.exploreCategory.model.raw.GQL_Affiliate_Performance
import com.tokopedia.exploreCategory.repository.AffiliateRepository
import javax.inject.Inject

class AffiliatePerformanceUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(userId: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_USER_ID] = userId
        return request
    }

    suspend fun affiliatePerformance(userId: String): AffiliatePerformanceData {
        return repository.getGQLData(
                GQL_Affiliate_Performance,
                AffiliatePerformanceData::class.java,
                createRequestParams(userId)
        )
    }

    companion object {
        private const val PARAM_USER_ID = "userID"
    }


}