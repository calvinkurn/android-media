package com.tokopedia.travel_slice.utils

/**
 * @author by jessica on 25/11/20
 */

object TravelSlicesQueries {
    val HOTEL_ORDER_LIST_QUERY = """
        query OrderListQuery(${'$'}orderCategory: OrderCategory, ${'$'}Page: Int!, ${'$'}PerPage: Int!) {
          orders(orderCategory:${'$'}orderCategory, Page:${'$'}Page, PerPage:${'$'}PerPage) {
            title
            id
            statusStr
            appLink
          }
        }
    """.trimIndent()
}