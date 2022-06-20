package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.domain.gqlqueries.LeaveQueueQuery
import com.tokopedia.chatbot.domain.pojo.leavequeue.LeaveQueueResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class LeaveQueueUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<LeaveQueueResponse>(graphqlRepository) {

    fun execute(
        onSuccess: (LeaveQueueResponse) -> Unit,
        onError: (Throwable) -> Unit,
        msgId: String,
        timestamp: String
    ) {
        try {
            this.setTypeClass(LeaveQueueResponse::class.java)
            this.setRequestParams(generateParam(msgId, timestamp))
            this.setGraphqlQuery(LeaveQueueQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

    private fun generateParam(msgId: String, timestamp: String): Map<String, String> {
        val requestParams = HashMap<String, String>()
        requestParams[MESSAGE_ID] = msgId
        requestParams[TIME_STAMP] = timestamp
        return requestParams
    }

    companion object {
        private val MESSAGE_ID: String = "msgID"
        private val TIME_STAMP: String = "timestamp"
    }

}