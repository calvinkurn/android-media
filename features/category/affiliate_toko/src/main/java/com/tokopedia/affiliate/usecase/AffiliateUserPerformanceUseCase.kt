package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateUserPerformaListItemData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_USER_PERFORMANCE
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateUserPerformanceUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(dayRange: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_DAY_RANGE] = dayRange
        return request
    }

    suspend fun affiliateUserperformance(dayRange: String): AffiliateUserPerformaListItemData {
        return repository.getGQLData(
                GQL_Affiliate_USER_PERFORMANCE,
            AffiliateUserPerformaListItemData::class.java,
                createRequestParams(dayRange)
        )
    }

    companion object {
        private const val PARAM_DAY_RANGE = "dayRange"
    }
}