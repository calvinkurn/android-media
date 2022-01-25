package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub

class GetChatPreAttachPayloadUseCaseStub(
        private val repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
) : GetChatPreAttachPayloadUseCase(repository, dispatchers) {

    var response: PreAttachPayloadResponse = PreAttachPayloadResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }
}