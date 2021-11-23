package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateTransactionHistoryData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Transaction_History
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateTransactionHistoryUseCase @Inject constructor(
        private val repository: AffiliateRepository
) {

    private fun createRequestParams(): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_FILTER] = ""
        return request
    }
    suspend fun getAffiliateTransactionHistory(startData: String, endData:String, page: Int, limit : Int = 20): AffiliateTransactionHistoryData {
        return repository.getGQLData(
                GQL_Affiliate_Transaction_History,
                AffiliateTransactionHistoryData::class.java,
                createRequestParams()
        )
    }

    companion object {
        private const val PARAM_FILTER = "filter"
    }

}