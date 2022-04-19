package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.usecase.CloseReminderTicker
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class CloseReminderTickerStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
) : CloseReminderTicker(repository, dispatcher) {

    var response: Any = Any()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

}