package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GQL_GET_TOP_BOT_NEW_SESSION
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetTopBotNewSessionQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_GET_TOP_BOT_NEW_SESSION
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
