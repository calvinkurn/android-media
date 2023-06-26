package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeUserStatus.HOME_USER_STATUS_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeUserStatus.HOME_USER_STATUS_QUERY_NAME

@GqlQuery(HOME_USER_STATUS_QUERY_NAME, HOME_USER_STATUS_QUERY)
object QueryHomeUserStatus {
    const val HOME_USER_STATUS_QUERY_NAME = "HomeUserStatusQuery"
    const val HOME_USER_STATUS_QUERY: String = "" +
            "query homeStatus(){\n" +
            "   status\n" +
            "}"
}
