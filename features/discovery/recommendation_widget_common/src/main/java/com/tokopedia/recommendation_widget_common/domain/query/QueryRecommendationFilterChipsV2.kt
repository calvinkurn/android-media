package com.tokopedia.recommendation_widget_common.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.recommendation_widget_common.domain.query.QueryRecommendationFilterChipsV2.RECOMMENDATION_FILTER_CHIPS_V2_QUERY
import com.tokopedia.recommendation_widget_common.domain.query.QueryRecommendationFilterChipsV2.RECOMMENDATION_FILTER_CHIPS_V2_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(RECOMMENDATION_FILTER_CHIPS_V2_QUERY_NAME, RECOMMENDATION_FILTER_CHIPS_V2_QUERY)
internal object QueryRecommendationFilterChipsV2 {
    const val RECOMMENDATION_FILTER_CHIPS_V2_QUERY_NAME = "RecommendationFilterChipsQueryV2"
    const val RECOMMENDATION_FILTER_CHIPS_V2_QUERY: String = "" +
        "query RecommendationFilterChipsQueryV2(${'$'}productIDs: String!, ${'$'}pageName: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}filterType: String!, ${'$'}injectionID: String!, ${'$'}userID: Int) { +\n" +
        "           recommendationFilterChipsV2(productIDs: ${'$'}productIDs, pageName: ${'$'}pageName, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, filterType: ${'$'}filterType, injectionID: ${'$'}injectionID, userID: ${'$'}userID) { +\n" +
        "             data { +\n" +
        "               filter { +\n" +
        "                 title +\n" +
        "                 name +\n" +
        "                 templateName +\n" +
        "                 isActivated +\n" +
        "                 value +\n" +
        "                 options { +\n" +
        "                   name +\n" +
        "                   icon +\n" +
        "                   key +\n" +
        "                   value +\n" +
        "                   inputType +\n" +
        "                   isActivated +\n" +
        "                   isPopular +\n" +
        "                   child { +\n" +
        "                     name +\n" +
        "                     icon +\n" +
        "                     key +\n" +
        "                     value +\n" +
        "                     inputType +\n" +
        "                     child { +\n" +
        "                       name +\n" +
        "                       icon +\n" +
        "                       key +\n" +
        "                       value +\n" +
        "                       inputType +\n" +
        "                     } +\n" +
        "                   } +\n" +
        "                 } +\n" +
        "               } +\n" +
        "               sort { +\n" +
        "                 name +\n" +
        "                 key +\n" +
        "                 value +\n" +
        "                 inputType +\n" +
        "               } +\n" +
        "             } +\n" +
        "           } +\n" +
        "         }"
}
