package com.tokopedia.chatbot.domain.gqlqueries

import com.tokopedia.chatbot.domain.gqlqueries.queries.SEND_CHAT_RATING_QUERY
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class SendChatRatingQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return SEND_CHAT_RATING_QUERY
    }

    override fun getTopOperationName(): String {
        return ""
    }
}