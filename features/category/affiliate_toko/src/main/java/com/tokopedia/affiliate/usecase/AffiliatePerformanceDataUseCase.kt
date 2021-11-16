package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.model.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance_List
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliatePerformanceDataUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParamsList(dateRangeRequest: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_DATE_RANGE] = dateRangeRequest
        return request
    }


    suspend fun affiliateItemPerformanceList(dateRangeRequest: String): AffiliatePerformanceListData{
        return repository.getGQLData(
            GQL_Affiliate_Performance_List,
            AffiliatePerformanceListData::class.java,
            createRequestParamsList(dateRangeRequest)
        )
    }

    companion object {
        private const val PARAM_DATE_RANGE= "dayRange"
    }
}