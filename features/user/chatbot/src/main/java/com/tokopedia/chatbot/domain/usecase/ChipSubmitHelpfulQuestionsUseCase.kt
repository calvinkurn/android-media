package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.gqlqueries.CHIP_GET_CHAT_RATING_LIST_QUERY
import com.tokopedia.chatbot.domain.gqlqueries.CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueResponse
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionInput
import com.tokopedia.chatbot.domain.pojo.submitoption.SubmitOptionListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

private const val INPUT = "input"

@GqlQuery("SubmitHelpfullQuestion", CHIP_SUBMIT_HELPFULL_QUESTION_MUTATION_QUERY)
class ChipSubmitHelpfulQuestionsUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(SubmitHelpfullQuestion.GQL_QUERY,
                SubmitOptionListResponse::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun generateParam(submitOptionInput: SubmitOptionInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = submitOptionInput
        return requestParams
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}