package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeIconV2.HOME_ICON_V2_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeIconV2.HOME_ICON_V2_QUERY_NAME

@GqlQuery(HOME_ICON_V2_QUERY_NAME, HOME_ICON_V2_QUERY)
object QueryHomeIconV2 {
    const val HOME_ICON_V2_QUERY_NAME = "HomeIconV2Query"
    const val HOME_ICON_V2_QUERY: String = "" +
            "query getHomeIconV2(\$param: String, \$location: String) {\n" +
            "  getHomeIconV2(param: \$param, location: \$location) {\n" +
            "    icons {\n" +
            "      id\n" +
            "      url\n" +
            "      name\n" +
            "      persona\n" +
            "      brandID\n" +
            "      applinks\n" +
            "      imageUrl\n" +
            "      buIdentifier\n" +
            "      campaignCode\n" +
            "      withBackground\n" +
            "      categoryPersona\n" +
            "      galaxyAttribution\n" +
            "    }\n" +
            "  }\n" +
            "}\n"
}
