package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.GetChatRepliesPojo
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
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
                GetChatRepliesPojo::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_MESSAGE_ID: String = "msgId"

        fun generateParam(messageId: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams.put(PARAM_MESSAGE_ID, messageId)
            return requestParams
        }
    }
}