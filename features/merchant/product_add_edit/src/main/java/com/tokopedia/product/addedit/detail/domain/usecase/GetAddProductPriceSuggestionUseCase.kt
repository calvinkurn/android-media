package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.GetAddProductPriceSuggestionResponse
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionParam
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAddProductPriceSuggestionUseCase @Inject constructor(repository: GraphqlRepository)
    : GraphqlUseCase<GetAddProductPriceSuggestionResponse>(repository) {

    companion object {
        const val PARAM_REQUEST = "request"
        private const val SUGGESTION_SIZE = 5
        private val query =
                """
                query PriceSuggestionSuggestedPriceGetByKeywordV2(${'$'}request: PriceSuggestionSuggestedPriceGetByKeywordRequest!) {
                  PriceSuggestionSuggestedPriceGetByKeywordV2(request: ${'$'}request) {
                    status
                    summary {
                      suggestedPrice
                      suggestedPriceMin
                      suggestedPriceMax
                    }
                    suggestions {
                      productId
                      displayPrice
                      imageURL
                      title
                      totalSold
                      rating
                    }
                  }
                }
                """.trimIndent()
    }

    private val requestParams = RequestParams.create()

    init {
        setGraphqlQuery(query)
        setTypeClass(GetAddProductPriceSuggestionResponse::class.java)
    }

    fun setPriceSuggestionParams(keyword: String, categoryL3: String) {
        val priceSuggestionParam = PriceSuggestionParam(keyword, categoryL3, SUGGESTION_SIZE)
        requestParams.putObject(PARAM_REQUEST, priceSuggestionParam)
        setRequestParams(requestParams.parameters)
    }
}