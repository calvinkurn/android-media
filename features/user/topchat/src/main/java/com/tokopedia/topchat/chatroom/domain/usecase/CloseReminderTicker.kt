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
            PARAM_FEATURE_ID to param.featureId
        )
    }

    override fun graphqlQuery(): String = """
        mutation CloseReminderTicker($$PARAM_FEATURE_ID: Int!) {
            CloseReminderTicker($PARAM_FEATURE_ID: $$PARAM_FEATURE_ID) {
                success
            }
        }
        """

    companion object {
        private const val PARAM_FEATURE_ID: String = "featureId"
    }
}