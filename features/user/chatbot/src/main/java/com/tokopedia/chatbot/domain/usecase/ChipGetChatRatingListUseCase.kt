package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.domain.gqlqueries.CHIP_GET_CHAT_RATING_LIST_QUERY
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListInput
import com.tokopedia.chatbot.domain.pojo.ratinglist.ChipGetChatRatingListResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

private const val INPUT = "input"

@GqlQuery("GetChatListRating", CHIP_GET_CHAT_RATING_LIST_QUERY)
class ChipGetChatRatingListUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase) {

    suspend fun getChatRatingList(requestParams: Map<String, Any>) : GraphqlResponse{
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