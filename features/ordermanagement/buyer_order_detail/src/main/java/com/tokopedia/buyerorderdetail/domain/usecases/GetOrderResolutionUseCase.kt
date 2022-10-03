package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailScope
import com.tokopedia.buyerorderdetail.domain.models.GetOrderResolutionRequestState
import com.tokopedia.buyerorderdetail.domain.models.GetResolutionTicketStatusResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@BuyerOrderDetailScope
@GqlQuery("GetOrderResolutionQuery", GetOrderResolutionUseCase.QUERY)
class GetOrderResolutionUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetResolutionTicketStatusResponse>
) {

    init {
        useCase.setTypeClass(GetResolutionTicketStatusResponse::class.java)
        useCase.setGraphqlQuery(GetOrderResolutionQuery())
    }

    private fun createRequestParam(orderId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putLong(PARAM_ORDER_ID, orderId)
        }.parameters
    }

    fun getOrderResolution(orderId: Long) = flow {
        emit(GetOrderResolutionRequestState.Requesting)
        useCase.setRequestParams(createRequestParam(orderId))
        emit(GetOrderResolutionRequestState.Success(useCase.executeOnBackground().resolutionGetTicketStatus?.data))
    }.catch {
        emit(GetOrderResolutionRequestState.Error(it))
    }.flowOn(Dispatchers.IO)

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
