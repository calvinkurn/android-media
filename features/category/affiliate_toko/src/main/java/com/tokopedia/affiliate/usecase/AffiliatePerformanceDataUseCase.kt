package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance_List
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliatePerformanceDataUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParamsList(dateRangeRequest: String, lastID: String, pageType: Int): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_DATE_RANGE] = dateRangeRequest
        request[PARAM_LAST_ID] = lastID
        request[PARAM_PAGE_TYPE] = pageType
        return request
    }


    suspend fun affiliateItemPerformanceList(dateRangeRequest: String,lastID: String, pageType: Int): AffiliatePerformanceListData {
        return repository.getGQLData(
            GQL_Affiliate_Performance_List,
            AffiliatePerformanceListData::class.java,
            createRequestParamsList(dateRangeRequest, lastID, pageType)
        )
    }

    companion object {
        private const val PARAM_DATE_RANGE = "dayRange"
        private const val PARAM_LAST_ID = "lastID"
        private const val PARAM_PAGE_TYPE = "pageType"
    }
}