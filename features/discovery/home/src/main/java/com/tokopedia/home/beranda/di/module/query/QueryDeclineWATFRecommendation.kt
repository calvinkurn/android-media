package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryDeclineWATFRecommendation.QUERY_DECLINE_WATF_RECOMMENDATION
import com.tokopedia.home.beranda.di.module.query.QueryDeclineWATFRecommendation.QUERY_DECLINE_WATF_RECOMMENDATION_NAME

@GqlQuery(QUERY_DECLINE_WATF_RECOMMENDATION_NAME, QUERY_DECLINE_WATF_RECOMMENDATION)
internal object QueryDeclineWATFRecommendation {
    const val QUERY_DECLINE_WATF_RECOMMENDATION_NAME = "DeclineWATFRecommendation"
    const val QUERY_DECLINE_WATF_RECOMMENDATION = "mutation declineWATFRecommendation(\$request: rechargeDeclineAboveTheFoldRecommendationRequest!) {\n" +
            "  rechargeDeclineAboveTheFoldRecommendation(declineRequest: \$request) {\n" +
            "    isError: IsError\n" +
            "    message: Message\n" +
            "  }\n" +
            "}\n"
}