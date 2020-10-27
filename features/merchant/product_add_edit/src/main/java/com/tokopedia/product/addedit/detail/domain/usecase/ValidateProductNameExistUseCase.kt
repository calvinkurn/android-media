package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductNameExistParam
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductNameExistResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ValidateProductNameExistUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<ValidateProductNameExistResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
        private val query =
                """
                mutation ProductValidateV3(${'$'}input: ProductInputV3!) {
                  ProductValidateV3(input: ${'$'}input) {
                    header {
                      messages
                      reason
                      errorCode
                    }
                    isSuccess
                    data {
                      productName
                    }
                  }
                }
                """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(ValidateProductNameExistResponse::class.java)
    }

    fun setParams(productName: String) {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_INPUT, ValidateProductNameExistParam(productName))
        setRequestParams(requestParams.parameters)
    }
}