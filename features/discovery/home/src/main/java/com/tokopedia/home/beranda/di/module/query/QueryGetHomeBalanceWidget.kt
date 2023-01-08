package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryGetHomeBalanceWidget.GET_HOME_BALANCE_WIDGET_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryGetHomeBalanceWidget.GET_HOME_BALANCE_WIDGET_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_HOME_BALANCE_WIDGET_QUERY_NAME, GET_HOME_BALANCE_WIDGET_QUERY)
internal object QueryGetHomeBalanceWidget {
    const val GET_HOME_BALANCE_WIDGET_QUERY_NAME = "GetHomeBalanceWidgetQuery"
    const val GET_HOME_BALANCE_WIDGET_QUERY: String = "" +
            "query getHomeBalanceWidget()\n" +
            "        {\n" +
            "           getHomeBalanceWidget{\n" +
            "               error" +
            "               balances{\n" +
            "                  title\n" +
            "                  type\n" +
            "                  data\n" +
            "               }\n" +
            "            }" +
            "        }"
}