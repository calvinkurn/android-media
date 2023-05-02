package com.tokopedia.notifications.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.utils.ErrorHandler

class TokoChatPushNotifCallbackUseCase(
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<TokoChatPushNotifCallbackUseCase.Param, Unit>(dispatchers.io) {

    override fun graphqlQuery(): String = """
        mutation tokochatPushCallback($$PARAM_TOKOCHAT_PN_ID: String!, $$PARAM_TIMESTAMP: String!) {
            tokochatPushCallback($PARAM_TOKOCHAT_PN_ID: $$PARAM_TOKOCHAT_PN_ID, $PARAM_TIMESTAMP: $$PARAM_TIMESTAMP) {
                    success
                }
        }
    """.trimIndent()

    override suspend fun execute(params: Param) {
        try {
            return repository.request(graphqlQuery(), params)
        } catch (error: Throwable) {
            ErrorHandler.Companion.getErrorMessage(context = null, e = error)
            return
        }
    }

    data class Param(
        @SerializedName("tokochatPNId")
        val pushNotifId: String = "",

        @SerializedName("timestamp")
        val timestamp: String = ""
    ) : GqlParam

    companion object {
        private const val PARAM_TOKOCHAT_PN_ID = "tokochatPNId"
        private const val PARAM_TIMESTAMP = "timestamp"
    }
}
