package com.tokopedia.tokofood.feature.ordertracking.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.usecase.RequestParams

internal object TokoFoodOrderDetailQuery: GqlQueryInterface {

    private const val OPERATION_NAME = "GetTokofoodOrderDetail"
    private const val ORDER_ID_KEY = "orderID"

    private val TOKO_FOOD_ORDER_DETAIL_QUERY = """
        query $OPERATION_NAME(${'$'}orderID : String!) {
          tokofoodOrderDetail(orderID: $${'$'}orderID) {
            orderStatus {
              status
              title
              subtitle
              iconName
            }
            eta {
              label
              time
            }
            merchant {
              displayName
              distanceInKm
            }
            destination {
              label
              phone
              info
            }
            items {
              displayName
              price
              quantity
              variants {
                displayName
                optionName
              }
              notes
            }
            additionalTickerInfo {
              level
              appText
            }
            actionButtons {
              label
              actionType
              appUrl
              webUrl
            }
            dotMenus {
              label
              actionType
              appUrl
              webUrl
            }
            driverDetails {
              name
              photoUrl
              licensePlateNumber
              karma {
                icon
                message
              }
            }
            invoice {
              invoiceNumber
              gofoodOrderNumber
            }
            payment {
              paymentMethod {
                label
                value
              }
              paymentDetails {
                label
                value
              }
              paymentAmount {
                label
                value
              }
              paymentDate
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

    override fun getQuery(): String = TOKO_FOOD_ORDER_DETAIL_QUERY

    override fun getTopOperationName(): String = OPERATION_NAME
}