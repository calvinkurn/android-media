package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeIcon.HOME_ICON_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeIcon.HOME_ICON_QUERY_NAME

@GqlQuery(HOME_ICON_QUERY_NAME, HOME_ICON_QUERY)
object QueryHomeIcon {
    const val HOME_ICON_QUERY_NAME = "HomeIconQuery"
    const val HOME_ICON_QUERY: String = "" +
            "query homeIcon(\$param: String, \$location: String)\n" +
            "        {\n" +
            "          dynamicHomeIcon {\n" +
            "            dynamicIcon(param: \$param, location: \$location) {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              name\n" +
            "              url\n" +
            "              imageUrl\n" +
            "              applinks\n" +
            "              bu_identifier\n" +
            "              campaignCode\n" +
            "              withBackground\n" +
            "            }\n" +
            "          }\n" +
            "        }"
}