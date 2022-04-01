package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateRecommendedProductData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Recommended_Product
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateRecommendedProductUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(identifier : String,page: Int, limit: Int): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_IDENTIFIER] = identifier
        request[PARAM_PAGE] = page
        request[PARAM_LIMIT] = limit
        return request
    }

    suspend fun affiliateGetRecommendedProduct(identifier : String,page: Int, limit: Int): AffiliateRecommendedProductData {
        return repository.getGQLData(
                GQL_Affiliate_Recommended_Product,
                AffiliateRecommendedProductData::class.java,
                createRequestParams(identifier,page,limit)
        )
    }

    companion object {
        private const val PARAM_IDENTIFIER = "identifier"
        private const val PARAM_PAGE = "page"
        private const val PARAM_LIMIT = "limit"
    }
}