package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class SubmitCsatRatingQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.SUBMIT_CSAT_RATING
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
