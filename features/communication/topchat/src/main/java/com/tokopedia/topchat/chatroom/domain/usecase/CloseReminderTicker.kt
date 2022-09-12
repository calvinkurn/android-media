package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

open class CloseReminderTicker @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetReminderTickerUseCase.Param, Unit>(dispatcher.io) {

    override suspend fun execute(params: GetReminderTickerUseCase.Param) {
        val param = generateParam(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun generateParam(param: GetReminderTickerUseCase.Param): Map<String, Any> {
        return mapOf(
            PARAM_FEATURE_ID to param.featureId,
            PARAM_IS_SELLER to param.isSeller,
            PARAM_MSG_ID to param.msgId
        )
    }

    override fun graphqlQuery(): String = """
        mutation CloseReminderTicker($$PARAM_FEATURE_ID: Int!, $$PARAM_IS_SELLER: Boolean, $$PARAM_MSG_ID: Int) {
            CloseReminderTicker($PARAM_FEATURE_ID: $$PARAM_FEATURE_ID, $PARAM_IS_SELLER: $$PARAM_IS_SELLER, $PARAM_MSG_ID: $$PARAM_MSG_ID) {
                success
            }
        }
        """

    companion object {
        private const val PARAM_FEATURE_ID: String = "featureId"
        private const val PARAM_IS_SELLER: String = "isSeller"
        private const val PARAM_MSG_ID: String = "msgId"
    }
}