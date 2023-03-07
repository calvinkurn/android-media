package com.tokopedia.chatbot.domain.gqlqueries

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