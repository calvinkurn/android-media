package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateEducationSearchArticleCardsResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateEducationSearchResultUseCase.Companion.GET_EDUCATION_SEARCH_RESULT
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery ("GetSearchEducationResult", GET_EDUCATION_SEARCH_RESULT)
class AffiliateEducationSearchResultUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(
        limit: Int,
        offset: Int?,
        keyword: String?,
        categoryId: Long?,
    ): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[LIMIT_KEY] = limit
        request[OFFSET_KEY] = offset ?: 0
        request[KEYWORD] = keyword ?: ""
        request[CATEGORY_ID_KEY] = categoryId ?: 0
        return request
    }

    suspend fun getEducationSearchResultCards(
        limit: Int = 1,
        offset: Int? = 0,
        keyword: String? = "",
        categoryId: Long? = 0
    ): AffiliateEducationSearchArticleCardsResponse {
        return repository.getGQLData(
            GetSearchEducationResult.GQL_QUERY,
            AffiliateEducationSearchArticleCardsResponse::class.java,
            createRequestParams(limit, offset, keyword, categoryId)
        )
    }

    companion object {
        private const val KEYWORD = "keyword"
        private const val LIMIT_KEY = "limit"
        private const val OFFSET_KEY = "offset"
        private const val CATEGORY_ID_KEY = "categoryID"
        const val GET_EDUCATION_SEARCH_RESULT =
            "query searchEducation(${'$'}keyword: [String], ${'$'}limit: Int, ${'$'}Offset: Int, ${'$'}categoryID: [Int] ) {\n" +
            "  searchEducation(filter: { Limit: ${'$'}limit, Offset: ${'$'}Offset, keyword:${'$'}keyword, contentSource:\"affiliate\", categoryID:${'$'}categoryID}){\n" +
            "data {\n" +
                "      status\n" +
                "      results {\n" +
                "        section {\n" +
                "          id\n" +
                "          title\n" +
                "          items {\n" +
                "            title\n" +
                "            description\n" +
                "            url\n" +
                "            appURL\n" +
                "            modifiedDate\n" +
                "            publishTime\n" +
                "            categories {\n" +
                "              id\n" +
                "              title\n" +
                "              level\n" +
                "            }\n" +
                "            thumbnail {\n" +
                "              desktop\n" +
                "              mobile\n" +
                "              android\n" +
                "              ios\n" +
                "            }\n" +
                "            attributes {\n" +
                "              read_time\n" +
                "            }\n" +
                "          }\n" +
                "          meta {\n" +
                "            limit\n" +
                "            offset\n" +
                "            totalHits\n" +
                "          }\n" +
                "          sort {\n" +
                "            id\n" +
                "            name\n" +
                "            value\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
            "  }\n" +
            "}"
    }
}
