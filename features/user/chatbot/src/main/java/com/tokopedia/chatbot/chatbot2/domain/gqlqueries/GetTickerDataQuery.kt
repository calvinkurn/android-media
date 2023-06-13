package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetTickerDataQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GET_TICKER_DATA
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
