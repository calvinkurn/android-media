package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatMoveTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.usecase.RearrangeTemplateUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class RearrangeTemplateUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): RearrangeTemplateUseCase(repository, dispatcher) {

    var response: ChatMoveTemplateResponse = ChatMoveTemplateResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(ChatMoveTemplateResponse::class.java, "error")
            }
            field = value
        }
}
