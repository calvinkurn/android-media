package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

/**
 * @author by nisie on 21/12/18.
 */

@GqlQuery(
    "post_rating",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.SEND_CHAT_RATING_QUERY
)
class SendChatRatingUseCase
@Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo>(graphqlRepository) {

    fun sendChatRating(
        onSuccess: (com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        messageId: String,
        rating: Int,
        element: ChatRatingUiModel
    ) {
        try {
            this.setTypeClass(com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo::class.java)
            val timestamp = element.replyTimeNano.toString()
            this.setRequestParams(generateParam(messageId, rating, timestamp))
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.SendChatRatingQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error, messageId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

    companion object {

        private val PARAM_MESSAGE_ID: String = "msgId"
        private val PARAM_RATING: String = "rating"
        private val PARAM_TIMESTAMP: String = "timestamp"

        fun generateParam(messageId: String, rating: Int, timestamp: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = if (messageId.isNotBlank()) messageId else "0"
            requestParams[PARAM_RATING] = rating
            requestParams[PARAM_TIMESTAMP] = timestamp
            return requestParams
        }
    }
}
