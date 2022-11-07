package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionParams
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionResponse
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOrderResolutionUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers, private val repository: GraphqlRepository
) : BaseGraphqlUseCase<GetOrderResolutionParams, GetOrderResolutionRequestState>(dispatchers) {

    override fun graphqlQuery() = QUERY

    override suspend fun execute(params: GetOrderResolutionParams) = flow {
        emit(GetOrderResolutionRequestState.Requesting)
        emit(GetOrderResolutionRequestState.Complete.Success(sendRequest(params).resolutionGetTicketStatus?.data))
    }.catch {
        emit(GetOrderResolutionRequestState.Complete.Error(it))
    }

    private fun createRequestParam(params: GetOrderResolutionParams): Map<String, Any> {
        return RequestParams.create().apply {
            putLong(PARAM_ORDER_ID, params.orderId)
        }.parameters
    }

    private suspend fun sendRequest(
        params: GetOrderResolutionParams
    ): GetOrderResolutionResponse {
        return repository.request(
            graphqlQuery(),
            createRequestParam(params),
            getCacheStrategy(params.shouldCheckCache)
        )
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
