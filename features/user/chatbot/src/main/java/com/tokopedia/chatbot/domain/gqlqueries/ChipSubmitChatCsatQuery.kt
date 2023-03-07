package com.tokopedia.chatbot.domain.gqlqueries

import com.tokopedia.chatbot.domain.gqlqueries.queries.CHIP_SUBMIT_CHAT_CSAT
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ChipSubmitChatCsatQuery:GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return CHIP_SUBMIT_CHAT_CSAT
    }

    override fun getTopOperationName(): String {
        return ""
    }
}