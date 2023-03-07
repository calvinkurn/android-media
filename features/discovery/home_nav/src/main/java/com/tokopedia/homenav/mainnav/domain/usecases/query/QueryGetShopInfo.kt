package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetShopInfo.GET_SHOP_INFO_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetShopInfo.GET_SHOP_INFO_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_SHOP_INFO_QUERY_NAME, GET_SHOP_INFO_QUERY)
internal object QueryGetShopInfo {
    const val GET_SHOP_INFO_QUERY_NAME = "GetShopInfoQuery"
    const val GET_SHOP_INFO_QUERY = "" +
        "query getShopInfo(\$input: NotificationRequest){" +
        "     userShopInfo{" +
        "        info{" +
        "          shop_name" +
        "          shop_id" +
        "        }" +
        "     }" +
        "     notifications(input: \$input) {" +
        "        sellerOrderStatus {" +
        "           newOrder" +
        "           readyToShip" +
        "           inResolution" +
        "        }" +
        "     }" +
        "}"
}
