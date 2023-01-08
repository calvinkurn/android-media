package com.tokopedia.recommendation_widget_common.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.recommendation_widget_common.domain.query.QueryRecommendationFilterChips.RECOMMENDATION_FILTER_CHIPS_QUERY
import com.tokopedia.recommendation_widget_common.domain.query.QueryRecommendationFilterChips.RECOMMENDATION_FILTER_CHIPS_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(RECOMMENDATION_FILTER_CHIPS_QUERY_NAME, RECOMMENDATION_FILTER_CHIPS_QUERY)
internal object QueryRecommendationFilterChips {
    const val RECOMMENDATION_FILTER_CHIPS_QUERY_NAME = "RecommendationFilterChipsQuery"
    const val RECOMMENDATION_FILTER_CHIPS_QUERY: String = "" +
        "query RecommendationFilterChipsQuery(${'$'}productIDs: String!, ${'$'}pageName: String!, ${'$'}xSource: String!, ${'$'}queryParam: String!, ${'$'}filterType: String!, ${'$'}injectionID: String!, ${'$'}userID: Int) { +\n" +
        "           recommendationFilterChips(productIDs: ${'$'}productIDs, pageName: ${'$'}pageName, xSource: ${'$'}xSource, queryParam: ${'$'}queryParam, filterType: ${'$'}filterType, injectionID: ${'$'}injectionID, userID: ${'$'}userID) { +\n" +
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
