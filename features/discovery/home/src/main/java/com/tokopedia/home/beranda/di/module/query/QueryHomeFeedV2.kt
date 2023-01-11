package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeFeedV2.HOME_FEED_V2_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeFeedV2.HOME_FEED_V2_QUERY_NAME

@GqlQuery(HOME_FEED_V2_QUERY_NAME, HOME_FEED_V2_QUERY)
internal object QueryHomeFeedV2 {
    const val HOME_FEED_V2_QUERY_NAME = "HomeFeedV2Query"
    const val HOME_FEED_V2_QUERY = "query getHomeRecommendationProductV2(\$sourceType: String, \$productPage: Int, \$location: String) {\n" +
        "  getHomeRecommendationProductV2(sourceType: \$sourceType, page: \"home\", type: \"banner,banner_ads,position\", productPage: \$productPage, location: \$location) {\n" +
        "    pageName\n" +
        "    hasNextPage\n" +
        "    products {\n" +
        "      id\n" +
        "      url\n" +
        "      name\n" +
        "      price\n" +
        "      rating\n" +
        "      applink\n" +
        "      clickUrl\n" +
        "      imageUrl\n" +
        "      isTopads\n" +
        "      priceInt\n" +
        "      clusterID\n" +
        "      productKey\n" +
        "      isWishlist\n" +
        "      wishlistUrl\n" +
        "      countReview\n" +
        "      slashedPrice\n" +
        "      ratingAverage\n" +
        "      trackerImageUrl\n" +
        "      slashedPriceInt\n" +
        "      discountPercentage\n" +
        "      recommendationType\n" +
        "      categoryBreadcrumbs\n" +
        "      shop {\n" +
        "        id\n" +
        "        url\n" +
        "        city\n" +
        "        name\n" +
        "        domain\n" +
        "        applink\n" +
        "        imageUrl\n" +
        "        reputation\n" +
        "      }\n" +
        "      badges {\n" +
        "        title\n" +
        "        imageUrl\n" +
        "      }\n" +
        "      freeOngkir {\n" +
        "        isActive\n" +
        "        imageUrl\n" +
        "      }\n" +
        "      labelGroup {\n" +
        "        url\n" +
        "        type\n" +
        "        title\n" +
        "        position\n" +
        "      }\n" +
        "    }\n" +
        "    banners {\n" +
        "      id\n" +
        "      url\n" +
        "      name\n" +
        "      target\n" +
        "      applink\n" +
        "      persona\n" +
        "      brandID\n" +
        "      imageUrl\n" +
        "      creativeName\n" +
        "      buAttribution\n" +
        "      categoryPersona\n" +
        "      galaxyAttribution\n" +
        "    }\n" +
        "    positions {\n" +
        "      type\n" +
        "    }\n" +
        "  }\n" +
        "}\n"
}
