package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.PriceSuggestionSuggestedPriceGetResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class PriceSuggestionSuggestedPriceGetUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<PriceSuggestionSuggestedPriceGetResponse>(repository) {

    companion object {
        const val PARAM_PRODUCT_ID = "productId"
        private val query =
                """
                query PriceSuggestionSuggestedPriceGet(${'$'}productId: Int!) {
                  PriceSuggestionSuggestedPriceGet(productID: ${'$'}productId) {
                    productId
                    suggestedPrice
                    suggestedPriceMax
                    suggestedPriceMin
                    price
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
        requestParams.putLong(PARAM_PRODUCT_ID, productId)
        setRequestParams(requestParams.parameters)
    }
}