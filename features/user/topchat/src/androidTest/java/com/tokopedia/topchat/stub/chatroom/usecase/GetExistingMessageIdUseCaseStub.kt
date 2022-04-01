package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetExistingMessageIdUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
) : GetExistingMessageIdUseCase(repository, dispatcher) {

    private val defaultResponsePath = "success_get_message_id.json"

    var response: GetExistingMessageIdPojo = GetExistingMessageIdPojo()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if (value.isNotEmpty()) {
                repository.createErrorMapResult(response::class.java, value)
            }
            field = value
        }

    init {
        response = defaultResponse
    }

    val defaultResponse: GetExistingMessageIdPojo
        get() = alterResponseOf(defaultResponsePath) { }

}