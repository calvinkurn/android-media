package com.tokopedia.cmhomewidget.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetCMHomeWidgetGQLQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_QUERY_GET_CM_HOME_WIDGET_DATA
    }

    override fun getTopOperationName(): String {
        return ""
    }
}