package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.GetExistingMessageIdPojo
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetExistingMessageIdUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub<GetExistingMessageIdUseCaseStub>,
    dispatcher: CoroutineDispatchers
): GetExistingMessageIdUseCase(repository, dispatcher) {

    var response: GetExistingMessageIdPojo = GetExistingMessageIdPojo()
        set(value) {
            repository.createMapResult(this, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if(value.isNotEmpty()) {
                repository.createErrorMapResult(this, value)
            }
            field = value
        }
}