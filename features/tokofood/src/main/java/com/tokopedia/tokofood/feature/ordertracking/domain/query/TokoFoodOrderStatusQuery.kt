package com.tokopedia.tokofood.feature.ordertracking.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

internal object TokoFoodOrderStatusQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "GetTokofoodOrderStatus"
    private const val ORDER_ID_KEY = "orderID"

    private val TOKO_FOOD_ORDER_STATUS_QUERY = """
        query ${OPERATION_NAME}(${'$'}orderID : String!) {
          tokofoodOrderDetail(orderID: $${'$'}orderID) {
            orderStatus {
              status
              title
              subtitle
              iconName
            }
            additionalTickerInfo {
              level
              appText
            }
            eta {
              label
              time
            }
            invoice {
              invoiceNumber
              gofoodOrderNumber
            }
          }
        }
    """.trimIndent()

    fun createRequestParamsOrderDetail(orderId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(ORDER_ID_KEY, orderId)
        }.parameters
    }

    override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)

    override fun getQuery(): String = TOKO_FOOD_ORDER_STATUS_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME

}