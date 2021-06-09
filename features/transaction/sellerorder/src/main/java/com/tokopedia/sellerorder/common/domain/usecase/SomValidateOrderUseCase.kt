package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.domain.model.SomValidateOrderRequest
import com.tokopedia.sellerorder.common.domain.model.SomValidateOrderResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class SomValidateOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomValidateOrderResponse.Data>) {

    init {
        useCase.setTypeClass(SomValidateOrderResponse.Data::class.java)
        useCase.setGraphqlQuery(QUERY)
    }

    suspend fun execute(param: SomValidateOrderRequest): Boolean {
        useCase.setRequestParams(composeParam(param).parameters)
        return useCase.executeOnBackground().result.orderIds.isNullOrEmpty()
    }

    private fun composeParam(param: SomValidateOrderRequest): RequestParams {
        return RequestParams.create().apply {
            putObject(KEY_PARAM_INPUT, param)
        }
    }

    companion object {
        private const val QUERY = """
            query ValidateAcceptOrder(${'$'}input: ValidateAcceptOrderRequest!) {
              validate_accept_order(input: ${'$'}input) {
                list_order_id
              }
            }
        """

        private const val KEY_PARAM_INPUT = "input"
    }
}