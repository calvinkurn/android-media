package com.tokopedia.product.addedit.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.constant.AddEditProductGqlConstants
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductParam
import com.tokopedia.product.addedit.detail.domain.model.ValidateProductResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ValidateProductUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<ValidateProductResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
    }

    private val requestParams = RequestParams.create()
    private val requestParamsObject = ValidateProductParam()

    init {
        setGraphqlQuery(AddEditProductGqlConstants.getValidateProductQuery())
        setTypeClass(ValidateProductResponse::class.java)
    }

    fun setParamsProductSku(productSku: String) {
        requestParamsObject.productSku = productSku
        requestParams.putObject(PARAM_INPUT, requestParamsObject)
        setRequestParams(requestParams.parameters)
    }
}