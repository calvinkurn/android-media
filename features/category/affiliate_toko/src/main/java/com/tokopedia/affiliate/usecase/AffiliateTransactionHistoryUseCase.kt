package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Transaction_History
import com.tokopedia.affiliate.model.request.FilterRequest
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateTransactionHistoryUseCase @Inject constructor(
        private val repository: AffiliateRepository
) {

    private fun createRequestParams(dateRange: Int, page: Int, limit: Int): Map<String, Any> {
       return mapOf<String,Any>(PARAM_FILTER to FilterRequest(dateRange,page,limit))
    }
    suspend fun getAffiliateTransactionHistory(dateRange: Int, page: Int, limit : Int = 10): AffiliateTransactionHistoryData {
        return repository.getGQLData(
                GQL_Affiliate_Transaction_History,
                AffiliateTransactionHistoryData::class.java,
                createRequestParams(dateRange,page,limit)
        )
    }

    companion object {
        private const val PARAM_FILTER = "Filter"
    }

}