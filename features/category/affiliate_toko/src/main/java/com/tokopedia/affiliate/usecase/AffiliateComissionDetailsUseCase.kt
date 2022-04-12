package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Commission
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
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


    companion object {
        private const val PARAM_TRANSACTION_ID = "transactionID"
    }
}