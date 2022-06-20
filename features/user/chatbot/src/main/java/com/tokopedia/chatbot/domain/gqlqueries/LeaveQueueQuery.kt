package com.tokopedia.chatbot.domain.gqlqueries

import com.tokopedia.chatbot.domain.gqlqueries.queries.GQL_LEAVE_QUEUE
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class LeaveQueueQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_LEAVE_QUEUE
    }

    override fun getTopOperationName(): String {
        return ""
    }
}