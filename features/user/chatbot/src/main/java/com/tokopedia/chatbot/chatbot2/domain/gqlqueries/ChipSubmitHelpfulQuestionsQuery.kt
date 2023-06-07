package com.tokopedia.chatbot.chatbot2.domain.gqlqueries

import com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY
import com.tokopedia.gql_query_annotation.GqlQueryInterface

class ChipSubmitHelpfulQuestionsQuery : GqlQueryInterface {
    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY
    }

    override fun getTopOperationName(): String {
        return ""
    }
}
