package com.tokopedia.chatbot.domain.gqlqueries

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