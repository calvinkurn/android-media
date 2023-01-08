package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeBannerV2.HOME_BANNER_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeBannerV2.HOME_BANNER_QUERY_NAME

@GqlQuery(HOME_BANNER_QUERY_NAME, HOME_BANNER_QUERY)
internal object QueryHomeBannerV2 {
    const val HOME_BANNER_QUERY_NAME = "HomeBannerV2Query"
    const val HOME_BANNER_QUERY: String = "query getHomeBannerV2(\$page: String, \$location: String, \$interval: String) {\n" +
        "  getHomeBannerV2(page: \$page, location: \$location, interval: \$interval) {\n" +
        "    banners {\n" +
        "      id\n" +
        "      url\n" +
        "      type\n" +
        "      title\n" +
        "      applink\n" +
        "      persona\n" +
        "      brandID\n" +
        "      imageUrl\n" +
        "      promoCode\n" +
        "      categoryID\n" +
        "      creativeName\n" +
        "      campaignCode\n" +
        "      topadsViewUrl\n" +
        "      categoryPersona\n" +
        "      galaxyAttribution\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
