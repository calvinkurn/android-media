package com.tokopedia.notifications.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase

class TokoChatPushNotifCallbackUseCase(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<TokoChatPushNotifCallbackUseCase.Param, Unit>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        mutation tokochatPushCallback($$PARAM_TOKOCHAT_PN_ID: String!, $$PARAM_TIMESTAMP: String!) {
            tokochatPushCallback(${'$'}$PARAM_TOKOCHAT_PN_ID: ${'$'}$PARAM_TOKOCHAT_PN_ID, ${'$'}$PARAM_TIMESTAMP: ${'$'}$PARAM_TIMESTAMP) {
                    success
                }
        }
    """.trimIndent()

    override suspend fun execute(params: Param) {
        repository.request<Unit, String>(graphqlQuery(), Unit)
    }

    data class Param(
        @SerializedName("tokochatPNId")
        val pushNotifId: String = "",

        @SerializedName("timestamp")
        val timestamp: String = ""
    )

    companion object {
        private const val PARAM_TOKOCHAT_PN_ID = "tokochatPNId"
        private const val PARAM_TIMESTAMP = "timestamp"
    }
}
