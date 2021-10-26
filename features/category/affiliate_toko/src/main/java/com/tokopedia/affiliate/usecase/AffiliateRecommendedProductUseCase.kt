package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliateRecommendedProductData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Recommended_Product
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateRecommendedProductUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(userID: String, identifier : String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_USERID] = userID
        request[PARAM_IDENTIFIER] = identifier
        return request
    }

    suspend fun affiliateGetRecommendedProduct(userID: String, identifier : String): AffiliateRecommendedProductData {
        return repository.getGQLData(
                GQL_Affiliate_Recommended_Product,
                AffiliateRecommendedProductData::class.java,
                createRequestParams(userID,identifier)
        )
    }

    companion object {
        private const val PARAM_USERID = "userID"
        private const val PARAM_IDENTIFIER = "identifier"
    }
}