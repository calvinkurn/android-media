package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.gql_query_annotation.GqlQueryInterface

class CheckUploadSecureQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.UPLOAD_SECURE_QUERY
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
