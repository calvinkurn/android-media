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
        "default_success_get_reminder_ticker.json"

    val defaultSrwPrompt: GetReminderTickerResponse
        get() = alterResponseOf(defaultResponse) { }

    val falseSrwPrompt: GetReminderTickerResponse
        get() = alterResponseOf(defaultResponse) {
            val ticker = it.getAsJsonObject(GetReminderTicker)
            ticker.addProperty(enable, false)
        }

    val noRegexMatchSrwPrompt: GetReminderTickerResponse
        get() = alterResponseOf(defaultResponse) {
            val ticker = it.getAsJsonObject(GetReminderTicker)
            ticker.addProperty(regexMessage, "random message not gonna happened")
        }

    companion object {
        const val GetReminderTicker = "GetReminderTicker"
        const val enable = "enable"
        const val regexMessage = "regexMessage"
    }
}