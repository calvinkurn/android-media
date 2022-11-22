package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.domain.gqlqueries.SendChatRatingQuery
import com.tokopedia.chatbot.domain.gqlqueries.queries.SEND_CHAT_RATING_QUERY
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject
import kotlin.reflect.KFunction3

/**
 * @author by nisie on 21/12/18.
 */

@GqlQuery("post_rating", SEND_CHAT_RATING_QUERY)
class SendChatRatingUseCase
@Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<SendRatingPojo>(graphqlRepository) {

    fun sendChatRating(
        onSuccess: KFunction3<SendRatingPojo, Int, ChatRatingUiModel, Unit>,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        messageId: String,
        rating: Int,
        element: ChatRatingUiModel
    ) {
        try {
            this.setTypeClass(SendRatingPojo::class.java)
            val timestamp = element.replyTimeNano.toString()
            this.setRequestParams(generateParam(messageId, rating, timestamp))
            this.setGraphqlQuery(SendChatRatingQuery())

            this.execute(
                { result ->
                    onSuccess(result, rating, element)
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
