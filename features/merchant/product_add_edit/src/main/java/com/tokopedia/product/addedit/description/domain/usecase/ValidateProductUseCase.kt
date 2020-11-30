package com.tokopedia.product.addedit.description.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.description.domain.model.ValidateProductDescriptionParam
import com.tokopedia.product.addedit.description.domain.model.ValidateProductDescriptionResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ValidateProductDescriptionUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<ValidateProductDescriptionResponse>(repository) {

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
                      description
                    }
                  }
                }
                """.trimIndent()
    }

    private val requestParams = RequestParams.create()
    private val requestParamsObject = ValidateProductDescriptionParam()

    init {
        setGraphqlQuery(query)
        setTypeClass(ValidateProductDescriptionResponse::class.java)
    }

    fun setParams(productDescription: String) {
        requestParamsObject.description = productDescription
        requestParams.putObject(PARAM_INPUT, requestParamsObject)
        setRequestParams(requestParams.parameters)
    }
}