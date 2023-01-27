package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GQL_UPLOAD_VIDEO_ELIGIBILITY
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ChatbotUploadVideoEligibilityQuery : GqlQueryInterface {

    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return GQL_UPLOAD_VIDEO_ELIGIBILITY
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
