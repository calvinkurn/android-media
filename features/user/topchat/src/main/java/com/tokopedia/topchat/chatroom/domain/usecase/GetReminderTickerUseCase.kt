package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.GetReminderTickerResponse
import javax.inject.Inject

open class GetReminderTickerUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetReminderTickerUseCase.Param, GetReminderTickerResponse>(dispatcher.io) {

    override suspend fun execute(params: Param): GetReminderTickerResponse {
        val param = generateParam(params)
        return repository.request(graphqlQuery(), param)
    }

    private fun generateParam(param: Param): Map<String, Any> {
        return mapOf(
            PARAM_FEATURE_ID to param.featureId
        )
    }

    override fun graphqlQuery(): String = """
            query GetReminderTicker($$PARAM_FEATURE_ID: Int!) {
                GetReminderTicker ($PARAM_FEATURE_ID: $$PARAM_FEATURE_ID) {
                    featureId
                    enable
                    mainText
                    subText
                    url
                    urlLabel
                    enableClose
                    regexMessage
                 }
            }
        """

    class Param(
        val featureId: Int = -1
    ) {
        companion object {
            const val SRW_TICKER = 1
        }
    }

    companion object {
        private const val PARAM_FEATURE_ID: String = "featureId"
    }
}