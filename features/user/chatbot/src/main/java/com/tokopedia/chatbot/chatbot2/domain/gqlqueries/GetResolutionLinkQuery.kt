package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class GetResolutionLinkQuery : GqlQueryInterface {

    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GET_RESO_LINK_QUERY
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
