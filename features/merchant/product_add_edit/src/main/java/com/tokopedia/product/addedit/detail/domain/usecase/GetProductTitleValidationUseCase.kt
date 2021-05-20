package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.GetProductTitleValidationResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetProductTitleValidationUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<GetProductTitleValidationResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
        private val query =
                """
                query getProductTitleValidation(${'$'}input: String!){
                  getProductTitleValidation(title: ${'$'}input) {
                    is_success
                    blacklist_keyword {
                      keyword
                      status
                    }
                    negative_keyword {
                      keyword
                      negative
                    }
                    typo_detection {
                      incorrect
                      correct
                    }
                  }
                }
                """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetProductTitleValidationResponse::class.java)
    }

    fun setParam(productName: String) {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_INPUT, productName)
        setRequestParams(requestParams.parameters)
    }
}