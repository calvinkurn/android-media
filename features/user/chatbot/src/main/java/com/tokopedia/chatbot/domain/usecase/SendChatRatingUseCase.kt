package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.chatrating.SendRatingPojo
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 21/12/18.
 */
class SendChatRatingUseCase @Inject constructor(val resources: Resources,
                                                private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_send_rating)
        val graphqlRequest = GraphqlRequest(query,
                SendRatingPojo::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
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

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}