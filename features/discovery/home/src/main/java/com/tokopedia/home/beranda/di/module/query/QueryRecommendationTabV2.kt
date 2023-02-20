package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryRecommendationTabV2.RECOMMENDATION_TAB_V2_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryRecommendationTabV2.RECOMMENDATION_TAB_V2_QUERY_NAME

@GqlQuery(RECOMMENDATION_TAB_V2_QUERY_NAME, RECOMMENDATION_TAB_V2_QUERY)
internal object QueryRecommendationTabV2 {
    const val RECOMMENDATION_TAB_V2_QUERY_NAME = "RecommendationTabV2Query"
    const val RECOMMENDATION_TAB_V2_QUERY: String =
        "query getHomeRecommendationTabV2(\$location: String) {\n" +
            "  getHomeRecommendationTabV2(location: \$location) {\n" +
            "    tabs {\n" +
            "      id\n" +
            "      name\n" +
            "      imageUrl\n" +
            "      sourceType\n" +
            "    }\n" +
            "  }\n" +
            "}"
}
