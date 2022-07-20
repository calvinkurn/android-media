package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidget.BUSINESS_WIDGET_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryBusinessWidget.BUSINESS_WIDGET_QUERY_NAME

@GqlQuery(BUSINESS_WIDGET_QUERY_NAME, BUSINESS_WIDGET_QUERY)
internal object QueryBusinessWidget {
    const val BUSINESS_WIDGET_QUERY_NAME = "BusinessWidgetQuery"
    const val BUSINESS_WIDGET_QUERY : String = "query HomeWidget() {\n" +
            "  home_widget {\n" +
            "    widget_tab {\n" +
            "      id\n" +
            "      name\n" +
            "    }\n" +
            "    widget_header{\n" +
            "        back_color\n" +
            "    }\n" +
            "  }\n" +
            "}\n"
}