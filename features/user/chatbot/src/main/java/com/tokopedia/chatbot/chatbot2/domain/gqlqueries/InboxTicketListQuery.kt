package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class InboxTicketListQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GQL_INBOX_LIST
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
