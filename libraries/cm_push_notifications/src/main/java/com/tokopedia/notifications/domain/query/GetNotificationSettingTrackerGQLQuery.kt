package com.tokopedia.notifications.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetNotificationSettingTrackerGQLQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_QUERY_SEND_NOTIF_SETTINGS_TRACKER_DATA
    }

    override fun getTopOperationName(): String {
        return ""
    }
}