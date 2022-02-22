package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QuerySalamCloseWidget.CLOSE_WIDGET_QUERY
import com.tokopedia.home.beranda.di.module.query.QuerySalamCloseWidget.CLOSE_WIDGET_QUERY_NAME

@GqlQuery(CLOSE_WIDGET_QUERY_NAME, CLOSE_WIDGET_QUERY)
internal object QuerySalamCloseWidget {
    const val CLOSE_WIDGET_QUERY_NAME = "CloseWidgetQuery"
    const val CLOSE_WIDGET_QUERY = "mutation CloseWidget(\$params: SalamCloseWidgetInput!) {\n" +
            "  salamCloseWidget(params: \$params) {\n" +
            "    ID\n" +
            "  }\n" +
            "}\n"
}