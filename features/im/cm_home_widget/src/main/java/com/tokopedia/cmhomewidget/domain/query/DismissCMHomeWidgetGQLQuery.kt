package com.tokopedia.cmhomewidget.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class DismissCMHomeWidgetGQLQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_QUERY_DISMISS_CM_HOME_WIDGET
    }

    override fun getTopOperationName(): String {
        return ""
    }
}