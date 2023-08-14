package com.tokopedia.home_component.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home_component.query.QueryMissionWidget.MISSION_WIDGET_QUERY
import com.tokopedia.home_component.query.QueryMissionWidget.MISSION_WIDGET_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(MISSION_WIDGET_QUERY_NAME, MISSION_WIDGET_QUERY)
internal object QueryMissionWidget {
    const val MISSION_WIDGET_QUERY_NAME = "MissionWidgetQuery"
    const val MISSION_WIDGET_QUERY = "query getHomeMissionWidget(\$type: String!, \$location: String!) {\n" +
        "  getHomeMissionWidget(type: \$type, location: \$location) {\n" +
        "    missions {\n" +
        "      id\n" +
        "      title\n" +
        "      subTitle\n" +
        "      applink\n" +
        "      imageURL\n" +
        "      pageName\n" +
        "      categoryID\n" +
        "      productID\n" +
        "      productName\n" +
        "      recommendationType\n" +
        "      buType\n" +
        "      isTopads\n" +
        "      isCarousel\n" +
        "      shopID\n" +
        "    }\n" +
        "  }\n" +
        "}\n"
}
