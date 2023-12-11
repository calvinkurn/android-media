package com.tokopedia.sellerorder.partial_order_fulfillment.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofEstimateRequestParams
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.GetPofRequestEstimateResponse
import com.tokopedia.sellerorder.partial_order_fulfillment.domain.model.RequestState
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPofEstimateUseCase @Inject constructor(
    dispatcher: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository
): FlowUseCase<GetPofEstimateRequestParams, RequestState<GetPofRequestEstimateResponse.Data>>(dispatcher.io) {
    companion object {
        private const val PARAM_INPUT = "input"
        private val QUERY = """
            query GetPofRequestEstimate($$PARAM_INPUT: POFRequestEstimateRequest!) {
              partial_order_fulfillment_request_estimate(input: $$PARAM_INPUT) {
                order_id
                detail_info {
                  order_dtl_id
                  product_id
                  quantity_request
                }
                pof_summary {
                  label
                  value_num
                  value_string
                }
                pof_final_estimation {
                  label
                  value_num
                  value_string
                  tooltip
                }
                pof_info {
                  has_info
                  text
                }
              }
            }
        """.trimIndent()
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: GetPofEstimateRequestParams) = flow {
        if (params.valid()) {
            emit(RequestState.Requesting)
            delay(params.delay.orZero())
            emit(RequestState.Success(sendRequest(params)))
        }
    }.catch {
        emit(RequestState.Error(it))
    }

    private fun createGqlParams(params: GetPofEstimateRequestParams): Map<String, Any?> {
        return RequestParams
            .create()
            .apply {
                putObject(PARAM_INPUT, params)
            }
            .parameters
    }

    private suspend fun sendRequest(
        params: GetPofEstimateRequestParams
    ): GetPofRequestEstimateResponse.Data {
        return repository.request(
            graphqlQuery(),
            createGqlParams(params),
            GraphqlCacheStrategy.Builder(params.cacheType).build()
        )
    }
}
