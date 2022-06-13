package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeDataRevamp.HOME_DATA_REVAMP_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeDataRevamp.HOME_DATA_REVAMP_QUERY_NAME

@GqlQuery(HOME_DATA_REVAMP_QUERY_NAME, HOME_DATA_REVAMP_QUERY)
object QueryHomeDataRevamp {
    const val HOME_DATA_REVAMP_QUERY_NAME = "HomeDataRevampQuery"
    const val HOME_DATA_REVAMP_QUERY: String = "" +
            "query homeData()\n" +
            "        {\n" +
            "        status\n" +
            "          homeFlag{\n" +
            "                event_time\n" +
            "                server_time\n" +
            "                flags(name: \"has_recom_nav_button,dynamic_icon_wrap,has_tokopoints,is_autorefresh\"){\n" +
            "                    name\n" +
            "                    is_active\n" +
            "                    integer_value\n" +
            "                }\n" +
            "            }\n" +
            "        }"
}