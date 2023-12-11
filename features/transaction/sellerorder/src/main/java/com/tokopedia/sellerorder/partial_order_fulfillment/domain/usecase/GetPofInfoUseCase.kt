package com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofInfoRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestInfoResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPofInfoUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository
) : FlowUseCase<GetPofInfoRequestParams, RequestState<GetPofRequestInfoResponse.Data>>(dispatchers.io) {
    companion object {
        private const val PARAM_INPUT = "input"
        private val QUERY = """
            query GetPofRequestInfo($$PARAM_INPUT: InfoRequestPOFRequest!) {
              info_request_partial_order_fulfillment(input: $$PARAM_INPUT) {
                order_id
                pof_status
                details_original {
                  order_id
                  order_dtl_id
                  product_id
                  quantity_request
                  quantity_checkout
                  total_price_request
                  total_price_checkout
                  product_name
                  product_price
                  product_quantity
                  product_picture
                }
                details_fulfilled {
                  order_id
                  order_dtl_id
                  product_id
                  quantity_request
                  quantity_checkout
                  total_price_request
                  total_price_checkout
                  product_name
                  product_price
                  product_quantity
                  product_picture
                }
                details_unfulfilled {
                  order_id
                  order_dtl_id
                  product_id
                  quantity_request
                  quantity_checkout
                  total_price_request
                  total_price_checkout
                  product_name
                  product_price
                  product_quantity
                  product_picture
                }
              }
            }
        """.trimIndent()
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetPofInfoRequestParams) = flow {
        if (params.valid()) {
            emit(RequestState.Requesting)
            emit(RequestState.Success(sendRequest(params)))
        }
    }.catch {
        emit(RequestState.Error(it))
    }

    private fun createGqlParams(params: GetPofInfoRequestParams): Map<String, Any?> {
        return RequestParams
            .create()
            .apply { putObject(PARAM_INPUT, params) }
            .parameters
    }

    private suspend fun sendRequest(
        params: GetPofInfoRequestParams
    ): GetPofRequestInfoResponse.Data {
        return runCatching<GetPofRequestInfoResponse.Data> {
            repository.request(
                graphqlQuery(),
                createGqlParams(params),
                GraphqlCacheStrategy.Builder(params.cacheType).build()
            )
        }.onFailure {
            delay(params.delay.orZero())
        }.onSuccess {
            delay(params.delay.orZero())
        }.getOrThrow()
    }
}
