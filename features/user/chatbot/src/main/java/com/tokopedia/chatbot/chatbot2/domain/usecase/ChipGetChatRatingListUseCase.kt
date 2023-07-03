package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.chatbot2.data.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import javax.inject.Inject

private const val INPUT = "input"

@GqlQuery(
    "GetChatListRating",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.CHIP_GET_CHAT_RATING_LIST_QUERY
)
class ChipGetChatRatingListUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    suspend fun getChatRatingList(requestParams: Map<String, Any>): GraphqlResponse {
        val gql = MultiRequestGraphqlUseCase()
        val request = GraphqlRequest(GetChatListRating.GQL_QUERY, ChipGetChatRatingListResponse::class.java, requestParams)
        gql.addRequest(request)
        return gql.executeOnBackground()
    }

    fun generateParam(chipGetChatRatingListInput: ChipGetChatRatingListInput): Map<String, Any> {
        val requestParams = HashMap<String, Any>()
        requestParams[INPUT] = chipGetChatRatingListInput
        return requestParams
    }
}
