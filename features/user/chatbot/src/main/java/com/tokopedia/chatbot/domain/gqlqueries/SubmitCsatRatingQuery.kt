package com.tokopedia.chatbot.domain.gqlqueries

import com.tokopedia.chatbot.domain.gqlqueries.queries.SUBMIT_CSAT_RATING
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class SubmitCsatRatingQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return SUBMIT_CSAT_RATING
    }

    override fun getTopOperationName(): String {
        return ""
    }
}