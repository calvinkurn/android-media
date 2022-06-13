package com.tokopedia.topchat.stub.common.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatlist.pojo.ChatDeleteStatus
import com.tokopedia.topchat.common.domain.MutationMoveChatToTrashUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class MutationMoveChatToTrashUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): MutationMoveChatToTrashUseCase(repository, dispatcher) {

    var response: ChatDeleteStatus = ChatDeleteStatus()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var errorMessage = ""
        set(value) {
            if(value.isNotEmpty()) {
                repository.createErrorMapResult(response::class.java, value)
            }
            field = value
        }
}