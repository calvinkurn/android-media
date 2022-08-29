package com.tokopedia.vouchercreation.shop.create.domain.usecase.validation

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherValidationPartial
import com.tokopedia.vouchercreation.shop.create.domain.model.validation.VoucherValidationPartialResponse
import com.tokopedia.vouchercreation.shop.create.view.uimodel.validation.Validation

abstract class BaseValidationUseCase<T : Validation> (private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<T>() {

    abstract val queryString: String

    abstract val validationClassType: Class<out T>

    override suspend fun executeOnBackground(): T {
        val request = GraphqlRequest(queryString, VoucherValidationPartialResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(VoucherValidationPartialResponse::class.java)
        if (error.isNullOrEmpty()) {
            val validationPartialResponse: VoucherValidationPartialResponse = response.getData()
            val validationPartial = validationPartialResponse.response.validationPartialErrorData.validationPartial
            return validationPartial.convertToUiModel()
        } else {
            throw MessageErrorException(error.first().toString())
        }
    }

    private fun VoucherValidationPartial.convertToUiModel(): T {
        val gson = Gson()
        val json = gson.toJson(this)
        return gson.fromJson(json, validationClassType)
    }
}