package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Education_Article_Cards
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateEducationArticleCardsUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(
        categoryId: Int,
        filter: String,
        limit: Int,
        offset: Int,
        sortBy: String
    ): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[SOURCE_KEY] = SOURCE
        request[FILTER_KEY] = filter
        request[CATEGORY_ID_KEY] = categoryId
        request[LIMIT_KEY] = limit
        request[OFFSET_KEY] = offset
        request[SORT_KEY] = sortBy
        return request
    }

    suspend fun getEducationArticleCards(
        categoryId: Int,
        filter: String = "latest",
        limit: Int = 0,
        offset: Int = 0,
        sortBy: String = "start_datetime"
    ): AffiliateEducationArticleCardsResponse {
        return repository.getGQLData(
            GQL_Affiliate_Education_Article_Cards,
            AffiliateEducationArticleCardsResponse::class.java,
            createRequestParams(categoryId, filter, limit, offset, sortBy)
        )
    }

    companion object {
        private const val SOURCE_KEY = "source"
        private const val SOURCE = "affiliate"
        private const val FILTER_KEY = "filter"
        private const val CATEGORY_ID_KEY = "category_id"
        private const val LIMIT_KEY = "limit"
        private const val OFFSET_KEY = "offset"
        private const val SORT_KEY = "sort_by"
    }
}
