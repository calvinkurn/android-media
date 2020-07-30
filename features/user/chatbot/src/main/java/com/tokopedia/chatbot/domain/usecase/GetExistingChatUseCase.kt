package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chatbot.R
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import java.lang.NumberFormatException
import javax.inject.Inject

/**
 * @author : Steven 30/11/18
 */

class GetExistingChatUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_chat_replies)
        val graphqlRequest = GraphqlRequest(query,
                GetExistingChatPojo::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_MESSAGE_ID: String = "msgId"
        private val PARAM_PAGE: String = "page"


        fun generateParamFirstTime(messageId: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = verifyMessageId(messageId)
            requestParams[PARAM_PAGE] = 1
            return requestParams
        }

        fun generateParam(messageId: String, page: Int): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = verifyMessageId(messageId)
            requestParams[PARAM_PAGE] = page
            return requestParams
        }

        private fun verifyMessageId(messageId: String): Int {
            return if (messageId.isNotBlank()) {
                try {
                    messageId.toInt()
                } catch (e: NumberFormatException) {
                    0
                }
            } else 0
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}