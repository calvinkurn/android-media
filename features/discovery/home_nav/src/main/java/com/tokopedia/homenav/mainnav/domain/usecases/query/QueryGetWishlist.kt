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
    const val GET_WISHLIST_QUERY = "" +
        "query GetWishlist(${'$'}params:WishlistV2Params){\n" +
        "              wishlist_v2(params:${'$'}params){\n" +
        "                has_next_page\n" +
        "                items {\n" +
        "                  id\n" +
        "                  name\n" +
        "                  url\n" +
        "                  image_url\n" +
        "                  price\n" +
        "                  price_fmt\n" +
        "                  available\n" +
        "                  label_status\n" +
        "                  preorder\n" +
        "                  rating\n" +
        "                  sold_count\n" +
        "                  min_order\n" +
        "                  shop {\n" +
        "                    id\n" +
        "                    name\n" +
        "                    url\n" +
        "                    location\n" +
        "                    fulfillment{\n" +
        "                      is_fulfillment\n" +
        "                      text\n" +
        "                    }\n" +
        "                    is_tokonow\n" +
        "                  }\n" +
        "                  badges{\n" +
        "                    title\n" +
        "                    image_url\n" +
        "                  }\n" +
        "                  labels\n" +
        "                  wholesale_price{\n" +
        "                    minimum\n" +
        "                    maximum\n" +
        "                    price\n" +
        "                  }\n" +
        "                  default_child_id\n" +
        "                  original_price\n" +
        "                  original_price_fmt\n" +
        "                  discount_percentage\n" +
        "                  discount_percentage_fmt\n" +
        "                  label_stock\n" +
        "                  bebas_ongkir {\n" +
        "                    type\n" +
        "                    title\n" +
        "                    image_url\n" +
        "                  }\n" +
        "                  label_group{\n" +
        "                    position\n" +
        "                    title\n" +
        "                    type\n" +
        "                    url\n" +
        "                  }\n" +
        "                  buttons{\n" +
        "                    primary_button{\n" +
        "                      text\n" +
        "                      action\n" +
        "                      url\n" +
        "                    }\n" +
        "                    additional_buttons{\n" +
        "                      text\n" +
        "                      action\n" +
        "                      url\n" +
        "                    }\n" +
        "                  }\n" +
        "                  wishlist_id\n" +
        "                  variant_name\n" +
        "                  category{\n" +
        "                    category_id\n" +
        "                    category_name\n" +
        "                  }\n" +
        "                }\n" +
        "                error_message\n" +
        "              }\n" +
        "            }"
}
