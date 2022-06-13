package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QuerySalamWidget.SALAM_WIDGET_QUERY
import com.tokopedia.home.beranda.di.module.query.QuerySalamWidget.SALAM_WIDGET_QUERY_NAME

@GqlQuery(SALAM_WIDGET_QUERY_NAME, SALAM_WIDGET_QUERY)
internal object QuerySalamWidget {
    const val SALAM_WIDGET_QUERY_NAME = "SalamWidgetQuery"
    const val SALAM_WIDGET_QUERY = "query salamWidget(){\n" +
            "                salamWidget{\n" +
            "                     ID\n" +
            "                     mainText\n" +
            "                     SubText\n" +
            "                     AppLink\n" +
            "                     Link\n" +
            "                     IconURL\n" +
            "                     Title\n" +
            "                     BackgroundColor\n" +
            "                     ButtonText\n" +
            "                  }\n" +
            "            }"
}