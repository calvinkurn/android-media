package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionSuggestedPriceGetResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class PriceSuggestionSuggestedPriceGetUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<PriceSuggestionSuggestedPriceGetResponse>(repository) {

    companion object {
        const val PARAM_PRODUCT_ID = "id"
        private val query =
                """
                query PriceSuggestionSuggestedPriceGet(${'$'}id: String) {
                  PriceSuggestionSuggestedPriceGet(id: ${'$'}id) {
                    suggestedPrice
                    suggestedPriceMin
                    suggestedPriceMax
                    price
                    productRecommendation {
                      productID
                      price
                      imageURL
                      sold
                      rating
                      title
                    }
                  }
                }
                """.trimIndent()
    }

    private val requestParams = RequestParams.create()

    init {
        setGraphqlQuery(query)
        setTypeClass(PriceSuggestionSuggestedPriceGetResponse::class.java)
    }

    fun setParamsProductId(productId: Long) {
        requestParams.putString(PARAM_PRODUCT_ID, productId.toString())
        setRequestParams(requestParams.parameters)
    }
}