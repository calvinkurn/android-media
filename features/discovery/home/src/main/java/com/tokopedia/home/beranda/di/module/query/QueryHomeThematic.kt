package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryHomeThematic.HOME_THEMATIC_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryHomeThematic.HOME_THEMATIC_QUERY_NAME

@GqlQuery(HOME_THEMATIC_QUERY_NAME, HOME_THEMATIC_QUERY)
internal object QueryHomeThematic {
    const val HOME_THEMATIC_QUERY_NAME = "HomeThematicQuery"
    const val HOME_THEMATIC_QUERY: String =
        "query getHomeThematic {\n" +
        "  getHomeThematic {\n" +
        "    thematic {\n" +
        "      isShown\n" +
        "      colorMode\n" +
        "      heightPercentage\n" +
        "      backgroundImageURL\n" +
        "      foregroundImageURL\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
