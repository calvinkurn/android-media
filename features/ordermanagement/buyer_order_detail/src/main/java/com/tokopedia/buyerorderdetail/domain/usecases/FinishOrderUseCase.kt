package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class FinishOrderUseCase @Inject constructor(private val useCase: GraphqlUseCase<FinishOrderResponse.Data>) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(FinishOrderResponse.Data::class.java)
    }

    suspend fun execute(params: FinishOrderParams): FinishOrderResponse.Data.FinishOrderBuyer {
        useCase.setRequestParams(createRequestParam(params))
        return useCase.executeOnBackground().finishOrderBuyer
    }

    private fun createRequestParam(params: FinishOrderParams): Map<String, Any> {
        return RequestParams.create().apply {
            putObject(PARAM_INPUT, params)
        }.parameters
    }

    companion object {
        private const val PARAM_INPUT = "input"

        private const val QUERY = """
            mutation FinishOrderBuyer(${'$'}input:FinishOrderBuyerRequest!) {
              finish_order_buyer(input: ${'$'}input) {
                success
                message
              }
            }
        """
    }
}