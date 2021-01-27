package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.domain.gqlqueries.CHIP_GET_CHAT_RATING_LIST_QUERY
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

private const val INPUT = "input"

@GqlQuery("GetChatListRating", CHIP_GET_CHAT_RATING_LIST_QUERY)
class ChipGetChatRatingListUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val graphqlRequest = GraphqlRequest(GetChatListRating.GQL_QUERY,
                ChipGetChatRatingListResponse::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun generateParam(chipGetChatRatingListInput: ChipGetChatRatingListInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = chipGetChatRatingListInput
        return requestParams
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}