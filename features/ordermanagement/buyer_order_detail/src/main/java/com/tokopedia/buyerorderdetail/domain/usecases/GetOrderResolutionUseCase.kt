package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetResolutionTicketStatusResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOrderResolutionUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers, private val repository: GraphqlRepository
) : FlowUseCase<Long, GetOrderResolutionRequestState>(dispatchers.io) {

    private fun createRequestParam(orderId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putLong(PARAM_ORDER_ID, orderId)
        }.parameters
    }

    private suspend fun sendRequest(
        params: Long
    ): GetResolutionTicketStatusResponse {
        return repository.request(graphqlQuery(), createRequestParam(params))
    }

    override fun graphqlQuery() = QUERY

    override suspend fun execute(params: Long) = flow {
        emit(GetOrderResolutionRequestState.Requesting)
        emit(GetOrderResolutionRequestState.Success(sendRequest(params).resolutionGetTicketStatus?.data))
    }.catch {
        emit(GetOrderResolutionRequestState.Error(it))
    }

    companion object {
        private const val PARAM_ORDER_ID = "orderID"

        const val QUERY = """
            query resolutionGetTicketStatus(${'$'}$PARAM_ORDER_ID: Int64!) {
              resolutionGetTicketStatus($PARAM_ORDER_ID: ${'$'}$PARAM_ORDER_ID) {
                data {
                  profile_picture
                  card_title
                  resolution_status {
                    status
                    text
                    font_color
                  }
                  deadline {
                    datetime
                    background_color
                    background_color_unify
                    show_deadline
                  }
                  description
                  redirect_path {
                    lite
                    android
                  }
                }
                messageError
                status
              }
            }
        """
    }
}
