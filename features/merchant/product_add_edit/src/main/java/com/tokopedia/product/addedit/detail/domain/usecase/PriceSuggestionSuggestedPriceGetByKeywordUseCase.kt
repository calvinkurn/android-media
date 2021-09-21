package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionSuggestedPriceGetByKeywordResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class PriceSuggestionSuggestedPriceGetByKeywordUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<PriceSuggestionSuggestedPriceGetByKeywordResponse>(repository) {

    companion object {
        const val PARAM_KEYWORD = "keyword"
        private val query =
                """
                query PriceSuggestionSuggestedPriceGetByKeyword(${'$'}keyword: String!) {
                  PriceSuggestionSuggestedPriceGetByKeyword(keyword: ${'$'}keyword) {
                    productId
                    suggestedPrice
                    suggestedPriceMax
                    suggestedPriceMin
                    price
                    title
                  }
                }
                """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(PriceSuggestionSuggestedPriceGetByKeywordResponse::class.java)
    }

    fun setParamsKeyword(keyword: String) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_KEYWORD, keyword)
        setRequestParams(requestParams.parameters)
    }
}