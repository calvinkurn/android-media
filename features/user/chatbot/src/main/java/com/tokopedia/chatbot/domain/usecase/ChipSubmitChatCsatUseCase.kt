package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.domain.gqlqueries.CHIP_SUBMIT_CHAT_CSAT
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatInput
import com.tokopedia.chatbot.domain.pojo.submitchatcsat.ChipSubmitChatCsatResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

private const val INPUT = "input"

@GqlQuery("ChipSubmitChatCsat", CHIP_SUBMIT_CHAT_CSAT)
class ChipSubmitChatCsatUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(ChipSubmitChatCsat.GQL_QUERY,
                ChipSubmitChatCsatResponse::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun generateParam(chipSubmitChatCsatInput: ChipSubmitChatCsatInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = chipSubmitChatCsatInput
        return requestParams
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}
