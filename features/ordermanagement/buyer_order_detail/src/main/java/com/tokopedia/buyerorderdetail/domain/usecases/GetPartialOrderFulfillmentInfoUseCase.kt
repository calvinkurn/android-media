package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.PartialOrderFulfillmentMapper
import com.tokopedia.buyerorderdetail.domain.models.PartialOrderFulfillmentResponse
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val PARTIAL_ORDER_FULFILLMENT_INFO_QUERY = """
    query InfoRespondPartialOrderFulfillment(${'$'}order_id: String!) {
      info_respond_partial_order_fulfillment(input: {
           order_id: ${'$'}order_id
      }){
        order_id
        total_unfulfill
        total_fulfilled
        header_info
        footer_info
        details_fulfilled{
          order_dtl_id
          product_id
          product_name
          product_price
          product_quantity
          product_picture
        }
        details_unfulfill{
          order_dtl_id
          product_id
          product_name
          product_price
          product_quantity_request
          product_quantity_checkout
          product_picture
        }
        estimate_info{
          title
          list_info{
            info
          }
        }
        summary {
          pof_details{
            label
            key
            value
          }
          estimate_refund{
            label
            key
            value
          }
        }
      }
    }
"""

@GqlQuery("PartialOrderFulfillmentInfoQuery", PARTIAL_ORDER_FULFILLMENT_INFO_QUERY)
class GetPartialOrderFulfillmentInfoUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<PartialOrderFulfillmentResponse>,
    private val mapper: PartialOrderFulfillmentMapper
) {

    init {
        useCase.setGraphqlQuery(PartialOrderFulfillmentInfoQuery())
        useCase.setTypeClass(PartialOrderFulfillmentResponse::class.java)
    }

    suspend fun execute(orderId: String): PartialOrderFulfillmentWrapperUiModel {
        useCase.setRequestParams(createRequestParams(orderId))
        return mapper.mapToPartialOrderFulfillmentUiModelList(
            useCase.executeOnBackground().infoRespondPartialOrderFulfillment
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
