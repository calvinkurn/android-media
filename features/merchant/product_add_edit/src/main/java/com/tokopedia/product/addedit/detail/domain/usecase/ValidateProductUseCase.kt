package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.constant.ProductValidateV3QueryConstant
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductParam
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ValidateProductUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<ValidateProductResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
        private const val OPERATION_PARAM = "${'$'}input: ProductInputV3!"
        private const val QUERY_PARAM = "input: ${'$'}input"
        private val QUERY_REQUEST = """
            header {
                messages
                reason
                errorCode
            }
            isSuccess
            data {
                productName
                sku
            }
        """.trimIndent()
        private val query = String.format(
                ProductValidateV3QueryConstant.BASE_QUERY,
                OPERATION_PARAM,
                QUERY_PARAM,
                QUERY_REQUEST
        )
    }

    private val requestParams = RequestParams.create()
    private val requestParamsObject = ValidateProductParam()

    init {
        setGraphqlQuery(query)
        setTypeClass(ValidateProductResponse::class.java)
    }

    fun setParamsProductSku(productSku: String) {
        requestParamsObject.productSku = productSku
        requestParams.putObject(PARAM_INPUT, requestParamsObject)
        setRequestParams(requestParams.parameters)
    }
}