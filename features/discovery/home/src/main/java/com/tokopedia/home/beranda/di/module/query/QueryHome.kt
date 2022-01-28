package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHome.HOME_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHome.HOME_QUERY_NAME

@GqlQuery(HOME_QUERY_NAME, HOME_QUERY)
internal object QueryHome {
    const val HOME_QUERY_NAME = "HomeQuery"
    const val HOME_QUERY: String = "" +
            "query homeData()\n" +
            "        {\n" +
            "        status\n" +
            "          ticker {\n" +
            "            meta {\n" +
            "              total_data\n" +
            "            }\n" +
            "            tickers\n" +
            "            {\n" +
            "              id\n" +
            "              title\n" +
            "              message\n" +
            "              color\n" +
            "              layout\n" +
            "              ticker_type\n" +
            "              title\n" +
            "            }\n" +
            "          }\n" +
            "          slides(device: 32) {\n" +
            "            meta { total_data }\n" +
            "            slides {\n" +
            "              id\n" +
            "              galaxy_attribution\n" +
            "              persona\n" +
            "              brand_id\n" +
            "              category_persona\n" +
            "              image_url\n" +
            "              redirect_url\n" +
            "              applink\n" +
            "              topads_view_url\n" +
            "              promo_code\n" +
            "              creative_name\n" +
            "              type\n" +
            "              category_id\n" +
            "              campaignCode\n" +
            "            }\n" +
            "          }\n" +
            "          dynamicHomeIcon {\n" +
            "            dynamicIcon {\n" +
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
            "            }\n" +
            "          }\n" +
            "          homeFlag{\n" +
            "                event_time\n" +
            "                server_time\n" +
            "                flags(name: \"has_recom_nav_button,dynamic_icon_wrap,has_tokopoints,is_autorefresh\"){\n" +
            "                    name\n" +
            "                    is_active\n" +
            "                }\n" +
            "            }\n" +
            "        }"
}