package com.tokopedia.homenav.mainnav.domain.usecases.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetHomeNavTokopoints.GET_HOME_NAV_TOKOPOINTS_QUERY
import com.tokopedia.homenav.mainnav.domain.usecases.query.QueryGetHomeNavTokopoints.GET_HOME_NAV_TOKOPOINTS_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(GET_HOME_NAV_TOKOPOINTS_QUERY_NAME, GET_HOME_NAV_TOKOPOINTS_QUERY)
internal object QueryGetHomeNavTokopoints {
    const val GET_HOME_NAV_TOKOPOINTS_QUERY_NAME = "GetHomeNavTokopointsQuery"
    const val GET_HOME_NAV_TOKOPOINTS_QUERY = "" +
        "query GetHomeNavTokopoints() {\n" +
        "  tokopointsStatusFiltered(filterKeys: [\"points\"], pointsExternalCurrency: \"IDR\", source: \"globalMenu\"){\n" +
        "    statusFilteredData {\n" +
        "      points {\n" +
        "        iconImageURL\n" +
        "        pointsAmount\n" +
        "        pointsAmountStr\n" +
        "        externalCurrencyAmount\n" +
        "        externalCurrencyAmountStr\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
