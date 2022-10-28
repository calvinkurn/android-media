package com.tokopedia.chatbot.domain.gqlqueries

import com.tokopedia.chatbot.domain.gqlqueries.queries.GQL_INBOX_LIST
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class InboxTicketListQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_INBOX_LIST
    }

    override fun getTopOperationName(): String {
        return ""
    }
}