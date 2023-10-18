package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetOrderHistoryMePage.GET_ORDER_HISTORY_ME_PAGE_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetOrderHistoryMePage.GET_ORDER_HISTORY_ME_PAGE_QUERY_NAME

/**
 * Created by dhaba
 */
@MePage
@GqlQuery(GET_ORDER_HISTORY_ME_PAGE_QUERY_NAME, GET_ORDER_HISTORY_ME_PAGE_QUERY)
internal object QueryGetOrderHistoryMePage {
    const val GET_ORDER_HISTORY_ME_PAGE_QUERY_NAME = "GetOrderHistoryMePageQuery"
    const val GET_ORDER_HISTORY_ME_PAGE_QUERY = "" +
        "query GetOrderHistory(${'$'}input:UOHOrdersRequest!){" +
        "  uohOrders(input:${'$'}input) {" +
        "    orders {" +
        "  orderUUID" +
        "  status" +
        "  metadata {" +
        "    queryParams" +
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
