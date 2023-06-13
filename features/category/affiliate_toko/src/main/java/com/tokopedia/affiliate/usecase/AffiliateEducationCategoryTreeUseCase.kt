package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Education_Category_TREE
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateEducationCategoryTreeUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[SOURCE_KEY] = SOURCE
        return request
    }

    suspend fun getEducationCategoryTree(): AffiliateEducationCategoryResponse {
        return repository.getGQLData(
            GQL_Affiliate_Education_Category_TREE,
            AffiliateEducationCategoryResponse::class.java,
            createRequestParams()
        )
    }

    companion object {
        private const val SOURCE_KEY = "source"
        private const val SOURCE = "affiliate"
    }
}
