package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliatePerformanceData
import com.tokopedia.affiliate.model.DateRangeRequest
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance_List
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
    private fun createRequestParamsList(dateRangeRequest: DateRangeRequest,page: Int, limit: Int): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_PAGE] = page
        request[PARAM_LIMIT] = limit
        request[PARAM_DATE_RANGE] = dateRangeRequest
        return request
    }

    suspend fun affiliatePerformance(page: Int, limit : Int): AffiliatePerformanceData {
        return repository.getGQLData(
                GQL_Affiliate_Performance,
                AffiliatePerformanceData::class.java,
                createRequestParams(page,limit)
        )
    }

    suspend fun affiliateItemPerformanceList(dateRangeRequest: DateRangeRequest,page: Int,limit: Int): AffiliatePerformanceData{
        return repository.getGQLData(
            GQL_Affiliate_Performance_List,
            AffiliatePerformanceData::class.java,
            createRequestParamsList(dateRangeRequest,page,limit)
        )
    }

    companion object {
        private const val PARAM_PAGE = "page"
        private const val PARAM_LIMIT = "limit"
        private const val PARAM_DATE_RANGE= "dateRange"
    }
}