package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetOrderHistory.GET_ORDER_HISTORY_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetOrderHistory.GET_ORDER_HISTORY_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_ORDER_HISTORY_QUERY_NAME, GET_ORDER_HISTORY_QUERY)
internal object QueryGetOrderHistory {
    const val GET_ORDER_HISTORY_QUERY_NAME = "GetOrderHistoryQuery"
    const val GET_ORDER_HISTORY_QUERY = "" +
        "query GetOrderHistory(${'$'}input:UOHOrdersRequest!){" +
        "  uohOrders(input:${'$'}input) {" +
        "    orders {" +
        "  orderUUID" +
        "  status" +
        "  metadata {" +
        "    detailURL {" +
        "      appURL" +
        "    }" +
        "    status {" +
        "      label" +
        "      textColor" +
        "      bgColor" +
        "    }" +
        "    products {" +
        "      title" +
        "      imageURL" +
        "      inline1 {" +
        "        label" +
        "        textColor" +
        "        bgColor" +
        "      }" +
        "      inline2 {" +
        "        label" +
        "        textColor" +
        "        bgColor" +
        "        }" +
        "    }" +
        "}" +
        "        }" +
        "    }" +
        "}"
}
