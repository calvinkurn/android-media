package com.tokopedia.travel_slice.flight.data

/**
 * @author by furqan on 26/11/2020
 */
object FlightSlicesQueries {
    val FLIGHT_ORDER_LIST = """
        query OrderListQuery(${'$'}orderCategory: OrderCategory,${'$'}Page: Int!,${'$'}PerPage: Int!,${'$'}ID: Int!,${'$'}OrderStatus: Int!) {
          orders(orderCategory:${'$'}orderCategory, Page:${'$'}Page, PerPage:${'$'}PerPage, ID:${'$'}ID, OrderStatus:${'$'}OrderStatus) {
            metaData {
              label
              value
            }
            title
            categoryName
            statusStr
            id
            items {
              imageUrl
            }
            paymentData {
              label
              value
            }
          }
        }
    """.trimIndent()
}