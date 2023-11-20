package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.mapper.GetBomGroupedOrderMapper
import com.tokopedia.buyerorderdetail.domain.models.GetBomGroupedOrderResponse
import com.tokopedia.buyerorderdetail.presentation.model.OwocGroupedOrderWrapper
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

const val GET_BOM_GROUPED_ORDER_QUERY = """
    query GetBomGroupedOrder(${'$'}tx_id: String!, ${'$'}order_id: String!) {
      get_bom_grouped_order(input: {
            tx_id: ${'$'}tx_id
            order_id: ${'$'}order_id
      }) {
        tx_id
        title
        orders {
          order_id
          invoice
          buttons {
            key
            display_name
            type
            variant
            url
          }
          details {
            bundle_icon
            addon_label
            addon_icon
            order_addons {
              order_id
              id
              name
              price
              quantity
              type
              image_url
            }
            bundle {
              bundle_id
              bundle_variant_id
              bundle_name
              order_detail {
                order_detail_id
                product_id
                product_name
                thumbnail
                price_text
                quantity
                addon {
                  order_id
                  id
                  name
                  price
                  quantity
                  type
                  image_url
                }
              }
            }
            non_bundle {
              order_detail_id
              product_id
              product_name
              thumbnail
              price_text
              quantity
              addon {
                order_id
                id
                name
                price
                quantity
                type
                image_url
              }
            }
          }
          shop {
            shop_name
            badge_url
          }
        }
        ticker {
          text
          type
          action_text
          action_key
          action_url
        }
      }
    }
"""

@GqlQuery("GetBomGroupedOrderQuery", GET_BOM_GROUPED_ORDER_QUERY)
class GetBomGroupedOrderUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetBomGroupedOrderResponse>,
    private val getBomGroupedOrderMapper: GetBomGroupedOrderMapper
) {
    init {
        useCase.setGraphqlQuery(GetBomGroupedOrderQuery())
        useCase.setTypeClass(GetBomGroupedOrderResponse::class.java)
    }

    suspend fun execute(
        txId: String,
        orderId: String,
    ): OwocGroupedOrderWrapper {
        useCase.setRequestParams(createRequestParams(txId, orderId))
        return getBomGroupedOrderMapper.mapToOwocGroupedOrderWrapper(
            useCase.executeOnBackground().getBomGroupedOrder
        )
    }

    private fun createRequestParams(txId: String, orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(TX_ID_KEY, txId)
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    companion object {
        private const val ORDER_ID_KEY = "order_id"
        private const val TX_ID_KEY = "tx_id"
    }
}
