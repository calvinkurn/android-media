package com.tokopedia.product.addedit.preview.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.product.addedit.common.constant.ProductValidateV3QueryConstant
import com.tokopedia.product.addedit.preview.data.model.params.ValidateProductNameParam
import com.tokopedia.product.addedit.preview.data.model.responses.ValidateProductNameResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ValidateProductNameUseCase @Inject constructor(
        repository: GraphqlRepository): GraphqlUseCase<ValidateProductNameResponse>(repository) {

    companion object {
        const val PARAM_INPUT = "input"
    }

    private val requestParams = RequestParams.create()
    private val requestParamsObject = ValidateProductNameParam()

    init {
        setGraphqlQuery(ProductValidateV3QueryConstant.getValidateProductNameQuery())
        setTypeClass(ValidateProductNameResponse::class.java)
    }

    fun setParamsProductName(productId: String?, productName: String?) {
        requestParamsObject.productId = productId
        requestParamsObject.productName = productName
        requestParams.putObject(PARAM_INPUT, requestParamsObject)
        setRequestParams(requestParams.parameters)
    }
}