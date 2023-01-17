package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.PartialOrderFulfillmentMapper
import com.tokopedia.buyerorderdetail.domain.models.PartialOrderFulfillmentResponse
import com.tokopedia.buyerorderdetail.domain.models.RejectPartialOrderFulfillmentResponse
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.RejectPartialOrderFulfillmentUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val REJECT_PARTIAL_ORDER_FULFILLMENT_QUERY = """
    mutation RejectPartialOrderFulfillment(${'$'}order_id: Int!) {
      reject_partial_order_fulfillment(input: {
          order_id: ${'$'}order_id
      }){
         success
      }
    }
"""

@GqlQuery("RejectPartialOrderFulfillmentQuery", REJECT_PARTIAL_ORDER_FULFILLMENT_QUERY)
class RejectPartialOrderFulfillmentUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<RejectPartialOrderFulfillmentResponse>,
    private val mapper: PartialOrderFulfillmentMapper
) {

    init {
        useCase.setGraphqlQuery(RejectPartialOrderFulfillmentQuery())
        useCase.setTypeClass(RejectPartialOrderFulfillmentResponse::class.java)
    }

    suspend fun execute(orderId: Long): RejectPartialOrderFulfillmentUiModel {
        useCase.setRequestParams(createRequestParams(orderId))
        return mapper.mapToRejectPartialOrderFulfillmentUiModel(
            useCase.executeOnBackground().rejectPartialOrderFulfillment
        )
    }

    private fun createRequestParams(orderId: Long): Map<String, Any> {
        return RequestParams.create().apply {
            putLong(ORDER_ID_KEY, orderId)
        }.parameters
    }


    companion object {
        private const val ORDER_ID_KEY = "order_id"
    }
}
