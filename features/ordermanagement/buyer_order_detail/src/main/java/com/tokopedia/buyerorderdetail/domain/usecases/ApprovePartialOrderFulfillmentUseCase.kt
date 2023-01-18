package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.PartialOrderFulfillmentMapper
import com.tokopedia.buyerorderdetail.domain.models.ApprovePartialOrderFulfillmentResponse
import com.tokopedia.buyerorderdetail.presentation.model.ApprovePartialOrderFulfillmentUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val APPROVE_PARTIAL_ORDER_FULFILLMENT_QUERY = """
    mutation ApprovePartialOrderFulfillment(${'$'}order_id: String!) {
      approve_partial_order_fulfillment(input: {
          order_id: ${'$'}order_id
      }){
         success
      }
    }
"""

@GqlQuery("ApprovePartialOrderFulfillmentQuery", APPROVE_PARTIAL_ORDER_FULFILLMENT_QUERY)
class ApprovePartialOrderFulfillmentUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<ApprovePartialOrderFulfillmentResponse>,
    private val mapper: PartialOrderFulfillmentMapper
) {

    init {
        useCase.setGraphqlQuery(ApprovePartialOrderFulfillmentQuery())
        useCase.setTypeClass(ApprovePartialOrderFulfillmentResponse::class.java)
    }

    suspend fun execute(orderId: String): ApprovePartialOrderFulfillmentUiModel {
        useCase.setRequestParams(createRequestParams(orderId))
        return mapper.mapToApprovePartialOrderFulfillmentUiModel(
            useCase.executeOnBackground().approvePartialOrderFulfillment
        )
    }

    private fun createRequestParams(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    companion object {
        private const val ORDER_ID_KEY = "order_id"
    }
}
