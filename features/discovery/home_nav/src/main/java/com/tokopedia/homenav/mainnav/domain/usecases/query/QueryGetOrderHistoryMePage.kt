package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetOrderHistoryMePage.GET_ORDER_HISTORY_ME_PAGE_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetOrderHistoryMePage.GET_ORDER_HISTORY_ME_PAGE_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_ORDER_HISTORY_ME_PAGE_QUERY_NAME, GET_ORDER_HISTORY_ME_PAGE_QUERY)
internal object QueryGetOrderHistoryMePage {
    const val GET_ORDER_HISTORY_ME_PAGE_QUERY_NAME = "GetOrderHistoryMePageQuery"
    const val GET_ORDER_HISTORY_ME_PAGE_QUERY = "" +
        "query GetOrderHistory(${'$'}input:UOHOrdersRequest!){\n" +
        "              uohOrders(input:${'$'}input) {\n" +
        "                orders {\n" +
        "                          orderUUID\n" +
        "                          status\n" +
        "                          metadata {\n" +
        "                            queryParams" +
        "                            detailURL {\n" +
        "                              appURL\n" +
        "                            }\n" +
        "                            status {\n" +
        "                              label\n" +
        "                              textColor\n" +
        "                              bgColor\n" +
        "                            }\n" +
        "                            products {\n" +
        "                              title\n" +
        "                              imageURL\n" +
        "                              inline1 {\n" +
        "                                label\n" +
        "                                textColor\n" +
        "                                bgColor\n" +
        "                              }\n" +
        "                              inline2 {\n" +
        "                                label\n" +
        "                                textColor\n" +
        "                                bgColor\n" +
        "                                }\n" +
        "                            }\n" +
        "                        }\n" +
        "                    }\n" +
        "                }\n" +
        "            }"
}
