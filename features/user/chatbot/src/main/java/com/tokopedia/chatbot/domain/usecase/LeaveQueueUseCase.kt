package com.tokopedia.chatbot.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import javax.inject.Inject

class LeaveQueueUseCase @Inject constructor(val resources: Resources,
                                            private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: Map<String, String>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_leave_queue)
        val graphqlRequest = GraphqlRequest(query,
                LeaveQueueResponse::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val MESSAGE_ID: String = "msgID"
        private val TIME_STAMP: String = "timestamp"

        fun generateParam(msgId:String,timestamp:String): Map<String, String> {
            val requestParams = HashMap<String, String>()
            requestParams[MESSAGE_ID] = msgId
            requestParams[TIME_STAMP] = timestamp
            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}