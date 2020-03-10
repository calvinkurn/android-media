package com.tokopedia.notifications.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.notifications.data.model.AttributionNotifier

typealias UseCase = GraphqlUseCase<AttributionNotifier>

class AttributionUseCase(private val useCase: UseCase) {

    fun execute(
            setParams: Map<String, Any>,
            onSuccess: (AttributionNotifier) -> Unit,
            onError: (Throwable) -> Unit
    ) {
        useCase.apply {
            setTypeClass(AttributionNotifier::class.java)
            setRequestParams(setParams)
            setGraphqlQuery(QUERY)
            execute(onSuccess, onError)
        }
    }

    fun params(transactionId: String?, receipientId: String?): Map<String, Any> {
        return hashMapOf<String, Any>().apply {
            put(PARAM_TRANSACTION_ID, transactionId?: "")
            put(PARAM_RECIPIENT_ID, receipientId?: "")
            put(PARAM_STATUS, "delivered")
        }
    }

    companion object {
        private const val PARAM_TRANSACTION_ID = "trans_id"
        private const val PARAM_RECIPIENT_ID = "recipient_id"
        private const val PARAM_STATUS = "status"

        private val QUERY = """
            mutation WebHookPushNotification(${'$'}trans_id: String!, ${'$'}recipient_id: String!, ${'$'}status: String!) {
                notifier_sendWebhookPushNotification(trans_id:${'$'}trans_id, recipient_id:${'$'}recipient_id, status:${'$'}status){
                      is_success
                      error_message
                }
            }
        """.trimIndent()
    }

}