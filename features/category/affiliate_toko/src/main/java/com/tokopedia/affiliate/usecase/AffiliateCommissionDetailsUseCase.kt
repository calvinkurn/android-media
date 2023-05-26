package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Commission
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Traffic_Cards
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTrafficCommissionCardDetails
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateCommissionDetailsUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(transactionID: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TRANSACTION_ID] = transactionID
        return request
    }

    suspend fun affiliateCommissionDetails(transactionID: String): AffiliateCommissionDetailsData {
        return repository.getGQLData(
            GQL_Affiliate_Commission,
            AffiliateCommissionDetailsData::class.java,
            createRequestParams(transactionID)
        )
    }

    suspend fun affiliateTrafficCardDetails(
        transactionDate: String,
        lastItem: String,
        type: String
    ): AffiliateTrafficCommissionCardDetails {
        return repository.getGQLData(
            GQL_Affiliate_Traffic_Cards,
            AffiliateTrafficCommissionCardDetails::class.java,
            createTrafficRequestParams(transactionDate, lastItem, type)
        )
    }

    private fun createTrafficRequestParams(
        transactionDate: String,
        lastItem: String,
        type: String
    ): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TRANSACTION_DATE] = transactionDate
        request[LAST_ID] = lastItem
        request[LIMIT] = LIMIT_COUNT
        request[PAGE_TYPE] = type
        return request
    }

    companion object {
        private const val PARAM_TRANSACTION_ID = "transactionID"
        private const val PARAM_TRANSACTION_DATE = "transactionDate"
        private const val LAST_ID = "lastID"
        private const val LIMIT = "limit"
        private const val PAGE_TYPE = "pageType"
        private const val LIMIT_COUNT = 10
    }
}
