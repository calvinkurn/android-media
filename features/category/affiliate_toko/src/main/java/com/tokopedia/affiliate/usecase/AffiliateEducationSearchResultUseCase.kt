package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Education_Article_Cards
import com.tokopedia.affiliate.model.response.AffiliateEducationArticleCardsResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateEducationSearchResultUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(
        limit: Int,
        offset: Int,
        keyword: String,
    ): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[SOURCE_KEY] = SOURCE
        request[LIMIT_KEY] = limit
        request[OFFSET_KEY] = offset
        request[KEYWORD] = keyword
        return request
    }

    suspend fun getEducationSearchResultCards(
        limit: Int = 1,
        offset: Int = 0,
        keyword: String = "",
    ): AffiliateEducationArticleCardsResponse {
        return repository.getGQLData(
            GQL_Affiliate_Education_Article_Cards,
            AffiliateEducationArticleCardsResponse::class.java,
            createRequestParams( limit, offset, keyword)
        )
    }

    companion object {
        private const val SOURCE_KEY = "source"
        private const val SOURCE = "affiliate"
        private const val KEYWORD = "keyword"
        private const val LIMIT_KEY = "limit"
        private const val OFFSET_KEY = "offset"
    }
}
