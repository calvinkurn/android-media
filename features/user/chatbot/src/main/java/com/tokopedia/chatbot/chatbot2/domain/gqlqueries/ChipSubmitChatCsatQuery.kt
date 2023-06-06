package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ChipSubmitChatCsatQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.CHIP_SUBMIT_CHAT_CSAT
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
