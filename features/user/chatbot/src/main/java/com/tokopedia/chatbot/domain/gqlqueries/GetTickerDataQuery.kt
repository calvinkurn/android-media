package com.tokopedia.chatbot.domain.gqlqueries

import com.tokopedia.chatbot.domain.gqlqueries.queries.GET_TICKER_DATA
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetTickerDataQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GET_TICKER_DATA
    }

    override fun getTopOperationName(): String {
        return ""
    }
}