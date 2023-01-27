package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetWishlist.GET_WISHLIST_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetWishlist.GET_WISHLIST_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_WISHLIST_QUERY_NAME, GET_WISHLIST_QUERY)
internal object QueryGetWishlist {
    const val GET_WISHLIST_QUERY_NAME = "GetWishlistQuery"
    const val GET_WISHLIST_QUERY = "query GetWishlist { " +
        "get_wishlist_collections {\n" +
        "    data {\n" +
        "      collections {\n" +
        "        id\n" +
        "        name\n" +
        "        total_item\n" +
        "        item_text\n" +
        "        images\n" +
        "      }\n" +
        "      total_collection\n" +
        "      is_empty_state\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
