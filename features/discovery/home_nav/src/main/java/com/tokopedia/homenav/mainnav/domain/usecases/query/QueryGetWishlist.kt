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
    const val GET_WISHLIST_QUERY = " " +
        "query GetWishlist(${'$'}params:WishlistV2Params){ " +
        "wishlist_v2(params:${'$'}params){ " +
        "has_next_page " +
        "items { " +
        "id " +
        "name " +
        "url " +
        "image_url " +
        "price " +
        "price_fmt " +
        "available " +
        "label_status " +
        "preorder " +
        "rating " +
        "sold_count " +
        "min_order " +
        "shop { " +
        "id " +
        "name " +
        "url " +
        "location " +
        "fulfillment{ " +
        "is_fulfillment " +
        "text " +
        "} " +
        "is_tokonow " +
        "} " +
        "badges{ " +
        "title " +
        "image_url " +
        "} " +
        "labels " +
        "wholesale_price{ " +
        "minimum " +
        "maximum " +
        "price " +
        "} " +
        "default_child_id " +
        "original_price " +
        "original_price_fmt " +
        "discount_percentage " +
        "discount_percentage_fmt " +
        "label_stock " +
        "bebas_ongkir { " +
        "type " +
        "title " +
        "image_url " +
        "} " +
        "label_group{ " +
        "position " +
        "title " +
        "type " +
        "url " +
        "} " +
        "buttons{ " +
        "primary_button{ " +
        "text " +
        "action " +
        "url " +
        "} " +
        "additional_buttons{ " +
        "text " +
        "action " +
        "url " +
        "} " +
        "} " +
        "wishlist_id " +
        "variant_name " +
        "category{ " +
        "category_id " +
        "category_name " +
        "} " +
        "} " +
        "error_message " +
        "}" +
        "}"
}
