package com.tokopedia.vouchercreation.create.domain.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherValidationPartial
import com.tokopedia.vouchercreation.create.domain.model.validation.VoucherValidationPartialResponse

abstract class BaseValidationUseCase<T : Any> (private val gqlRepository: GraphqlRepository) : UseCase<T>() {

    abstract val queryString: String

    abstract val validationClassType: Class<out T>

    open var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): T {
        val request = GraphqlRequest(queryString, VoucherValidationPartialResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(VoucherValidationPartialResponse::class.java)
        if (error.isNullOrEmpty()) {
            val validationPartialResponse: VoucherValidationPartialResponse = response.getData(VoucherValidationPartialResponse::class.java)
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