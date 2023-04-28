package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Education_Category_TREE
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.url.TokopediaUrl
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
        private val isStaging = TokopediaUrl.getInstance().GQL.contains("staging")
        private val TYPE_ARTICLE_TOPIC = if (isStaging) 1222 else 381
        private val TYPE_EVENT_L1 = if (isStaging) 1223 else 382
        private val TYPE_TUTORIAL = if (isStaging) 1224 else 383
    }
}
