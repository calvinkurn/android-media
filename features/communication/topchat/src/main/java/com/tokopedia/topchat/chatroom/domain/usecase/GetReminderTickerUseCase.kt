package com.tokopedia.topchat.chatroom.domain.usecase

import com.google.gson.annotations.SerializedName
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
            PARAM_FEATURE_ID to param.featureId,
            PARAM_IS_SELLER to param.isSeller,
            PARAM_MSG_ID to param.msgId
        )
    }

    override fun graphqlQuery(): String = """
            query GetReminderTicker($$PARAM_FEATURE_ID: Int!, $$PARAM_IS_SELLER: Boolean, $$PARAM_MSG_ID: Int) {
                GetReminderTicker ($PARAM_FEATURE_ID: $$PARAM_FEATURE_ID, $PARAM_IS_SELLER: $$PARAM_IS_SELLER, $PARAM_MSG_ID: $$PARAM_MSG_ID) {
                    featureId
                    replyId
                    tickerType
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

    data class Param(
        @SerializedName(PARAM_FEATURE_ID)
        val featureId: Long,
        @SerializedName(PARAM_IS_SELLER)
        val isSeller: Boolean,
        @SerializedName(PARAM_MSG_ID)
        val msgId: Long
    )

    companion object {
        private const val PARAM_FEATURE_ID: String = "featureId"
        private const val PARAM_IS_SELLER: String = "isSeller"
        private const val PARAM_MSG_ID: String = "msgId"

        const val FEATURE_ID_GENERAL: Long = 0
    }
}