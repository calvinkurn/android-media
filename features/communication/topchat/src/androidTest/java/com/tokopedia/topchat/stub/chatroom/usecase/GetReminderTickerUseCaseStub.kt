package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker.GetReminderTickerResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetReminderTickerUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetReminderTickerUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
) : GetReminderTickerUseCase(repository, dispatcher) {

    var response: GetReminderTickerResponse = GetReminderTickerResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    private val defaultResponse =
        "ticker_reminder/success_get_reminder_ticker.json"

    val defaultTickerReminder: GetReminderTickerResponse
        get() = alterResponseOf(defaultResponse) { }

    val falseSrwPrompt: GetReminderTickerResponse
        get() = alterResponseOf(defaultResponse) {
            val ticker = it.getAsJsonObject(GET_REMINDER_TICKER)
            ticker.addProperty(ENABLE, false)
        }

    val noRegexMatchSrwPrompt: GetReminderTickerResponse
        get() = alterResponseOf(defaultResponse) {
            val ticker = it.getAsJsonObject(GET_REMINDER_TICKER)
            ticker.addProperty(REGEX_MESSAGE, "random message not gonna happened")
        }

    fun customTickerReminder(
        featureId: Long = FEATURE_ID_GENERAL,
        mainText: String = "",
        subText: String = "",
        url: String = "",
        urlLabel: String = "",
        enableClose: Boolean = true,
        replyId: String = "-1",
        tickerType: String = ""
    ): GetReminderTickerResponse {
        return alterResponseOf(defaultResponse) {
            val ticker = it.getAsJsonObject(GET_REMINDER_TICKER)
            ticker.addProperty(FEATURE_ID, featureId)
            ticker.addProperty(MAIN_TEXT, mainText)
            ticker.addProperty(SUB_TEXT, subText)
            ticker.addProperty(URL, url)
            ticker.addProperty(URL_LABEL, urlLabel)
            ticker.addProperty(ENABLE_CLOSE, enableClose)
            ticker.addProperty(REPLY_ID, replyId)
            ticker.addProperty(TICKER_TYPE, tickerType)
            ticker.addProperty(REGEX_MESSAGE, "")
        }
    }


    companion object {
        private const val GET_REMINDER_TICKER = "GetReminderTicker"
        private const val ENABLE = "enable"
        private const val REGEX_MESSAGE = "regexMessage"
        private const val FEATURE_ID = "featureId"
        private const val MAIN_TEXT = "mainText"
        private const val SUB_TEXT = "subText"
        private const val URL = "url"
        private const val URL_LABEL = "urlLabel"
        private const val ENABLE_CLOSE = "enableClose"
        private const val REPLY_ID = "replyId"
        private const val TICKER_TYPE = "tickerType"
    }
}