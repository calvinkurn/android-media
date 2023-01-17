package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetUserShopFollow.GET_USER_SHOP_FOLLOW_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetUserShopFollow.GET_USER_SHOP_FOLLOW_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_USER_SHOP_FOLLOW_QUERY_NAME, GET_USER_SHOP_FOLLOW_QUERY)
object QueryGetUserShopFollow {
    const val GET_USER_SHOP_FOLLOW_QUERY_NAME = "GetUserShopFollowQuery"
    const val GET_USER_SHOP_FOLLOW_QUERY = "" +
        "query GetUserShopFollow(\$userID: Int, \$perPage: Int){ " +
        "userShopFollow(input:{userID:\$userID, perPage:\$perPage}){ " +
        "result { " +
        "userShopFollowDetail{ " +
        "shopID " +
        "shopName " +
        "location " +
        "logo " +
        "badge{ " +
        "title " +
        "imageURL " +
        "} " +
        "reputation{ " +
        "score " +
        "tooltip " +
        "reputationScore " +
        "minBadgeScore " +
        "badge " +
        "badgeLevel " +
        "} " +
        "} " +
        "haveNext " +
        "} " +
        "} " +
        "}"
}
