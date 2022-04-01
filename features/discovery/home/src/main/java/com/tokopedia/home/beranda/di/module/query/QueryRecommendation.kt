package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryRecommendation.RECOMMENDATION_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryRecommendation.RECOMMENDATION_QUERY_NAME

@GqlQuery(RECOMMENDATION_QUERY_NAME, RECOMMENDATION_QUERY)
internal object QueryRecommendation {
    const val RECOMMENDATION_QUERY_NAME = "RecommendationQuery"
    const val RECOMMENDATION_QUERY: String =
        "query getRecommendation(\$location: String)\n" +
                " {\n" +
                "  get_home_recommendation(location: \$location){\n" +
                "    recommendation_tabs{\n" +
                "      id\n" +
                "      name\n" +
                "      image_url\n" +
                "    }\n" +
                "  }\n" +
                " }"
}