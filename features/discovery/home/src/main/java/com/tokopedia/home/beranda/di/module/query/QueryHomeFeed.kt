package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeFeed.HOME_FEED_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeFeed.HOME_FEED_QUERY_NAME

@Deprecated("has been migrated to QueryHomeFeedV2")
@GqlQuery(HOME_FEED_QUERY_NAME, HOME_FEED_QUERY)
internal object QueryHomeFeed {
    const val HOME_FEED_QUERY_NAME = "HomeFeedQuery"
    const val HOME_FEED_QUERY = "query getHomeRecom(\$recomID: Int!, \$count: Int!, \$page: Int!, \$location:String!){\n" +
        "  get_home_recommendation(location: \$location){\n" +
        "    recommendation_product(recomID:\$recomID, count:\$count, page:\$page, type:\"banner,banner_ads,position\"){\n" +
        "      pageName\n" +
        "      product{\n" +
        "        id\n" +
        "          name\n" +
        "          category_breadcrumbs\n" +
        "          url\n" +
        "          click_url\n" +
        "          image_url\n" +
        "          is_wishlist\n" +
        "          wishlist_url\n" +
        "          applink\n" +
        "          is_topads\n" +
        "          tracker_image_url\n" +
        "          price\n" +
        "          price_int\n" +
        "          clusterID\n" +
        "          discount_percentage\n" +
        "          slashed_price\n" +
        "          slashed_price_int\n" +
        "          rating\n" +
        "          ratingAverage\n" +
        "          count_review\n" +
        "          recommendation_type\n" +
        "          shop{\n" +
        "            id\n" +
        "            name\n" +
        "            url\n" +
        "            applink\n" +
        "            city\n" +
        "            reputation\n" +
        "          }\n" +
        "          labels{\n" +
        "            title\n" +
        "            color\n" +
        "          }\n" +
        "          label_group {\n" +
        "            type\n" +
        "            title\n" +
        "            position\n" +
        "            url\n" +
        "          }\n" +
        "          badges{\n" +
        "            title\n" +
        "            image_url\n" +
        "          }\n" +
        "          free_ongkir {\n" +
        "            is_active\n" +
        "            image_url\n" +
        "          }\n" +
        "      }\n" +
        "      banner {\n" +
        "            id\n" +
        "            galaxy_attribution\n" +
        "            persona\n" +
        "            brand_id\n" +
        "            name\n" +
        "            image_url\n" +
        "            url\n" +
        "            applink\n" +
        "            bu_attribution\n" +
        "            creative_name\n" +
        "            target\n" +
        "      }\n" +
        "      position {\n" +
        "            type\n" +
        "      }\n" +
        "      has_next_page\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
