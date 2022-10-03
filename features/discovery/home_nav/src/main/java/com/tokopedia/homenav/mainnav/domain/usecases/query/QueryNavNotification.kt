package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryNavNotification.NAV_NOTIFICATION_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryNavNotification.NAV_NOTIFICATION_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(NAV_NOTIFICATION_QUERY_NAME, NAV_NOTIFICATION_QUERY)
internal object QueryNavNotification {
    const val NAV_NOTIFICATION_QUERY_NAME = "NavNotificationQuery"
    const val NAV_NOTIFICATION_QUERY = "" +
        "query NavNotification(\$input: NotificationRequest){\n" +
        "        notifications(input: \$input){\n" +
        "               resolutionAs {\n" +
        "               buyer\n" +
        "            }\n" +
        "            inbox {\n" +
        "               inbox_ticket\n" +
        "               inbox_review\n" +
        "            }\n" +
        "    }\n" +
        "}"
}
