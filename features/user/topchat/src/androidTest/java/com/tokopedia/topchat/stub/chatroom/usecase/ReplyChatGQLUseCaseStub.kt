package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.domain.pojo.ChatReplyPojo
import com.tokopedia.topchat.chatroom.domain.usecase.ReplyChatGQLUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import com.tokopedia.topchat.stub.common.GraphqlUseCaseStub
import javax.inject.Inject

class ReplyChatGQLUseCaseStub @Inject constructor(
        private val repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
): ReplyChatGQLUseCase(repository, dispatchers) {

    var delayResponse = 0L
    var response = ChatReplyPojo()
        set(value) {
            repository.delayMs = delayResponse
            repository.createMapResult(response::class.java, value)
            field = value
        }
}