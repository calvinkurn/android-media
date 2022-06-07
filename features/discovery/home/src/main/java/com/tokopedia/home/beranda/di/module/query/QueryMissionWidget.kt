package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryMissionWidget.MISSION_WIDGET_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryMissionWidget.MISSION_WIDGET_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(MISSION_WIDGET_QUERY_NAME, MISSION_WIDGET_QUERY)
internal object QueryMissionWidget {
    const val MISSION_WIDGET_QUERY_NAME = "MissionWidgetQuery"
    const val MISSION_WIDGET_QUERY = "query getHomeMissionWidget() {\n" +
            "  getHomeMissionWidget{\n" +
            "    missions{\n" +
            "      id\n" +
            "      title\n" +
            "      subTitle\n" +
            "      applink\n" +
            "      URL\n" +
            "      imageURL\n" +
            "    }\n" +
            "  }\n" +
            "}"
}