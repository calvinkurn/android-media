package com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.SendPofResponse
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendPofUseCase @Inject constructor(
    dispatcher: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository
) : FlowUseCase<SendPofRequestParams, RequestState<SendPofResponse.Data>>(dispatcher.io) {
    companion object {
        private const val PARAM_INPUT = "input"
        private val QUERY = """
            mutation RequestPartialOrderFulfillment($$PARAM_INPUT: RequestPartialOrderFulfillmentArgs!) {
              request_partial_order_fulfillment(input:$$PARAM_INPUT){
                success
              }
            }
        """.trimIndent()
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: SendPofRequestParams) = flow {
        if (params.valid()) {
            emit(RequestState.Requesting)
            emit(RequestState.Success(sendRequest(params)))
        }
    }.catch {
        emit(RequestState.Error(it))
    }

    private fun createGqlParams(params: SendPofRequestParams): Map<String, Any?> {
        return RequestParams
            .create()
            .apply {
                putObject(PARAM_INPUT, params)
            }
            .parameters
    }

    private suspend fun sendRequest(
        params: SendPofRequestParams
    ): SendPofResponse.Data {
        return repository.request(graphqlQuery(), createGqlParams(params))
    }
}
