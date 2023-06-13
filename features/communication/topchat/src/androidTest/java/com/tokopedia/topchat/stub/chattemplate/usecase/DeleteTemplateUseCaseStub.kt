package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatDeleteTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class DeleteTemplateUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): DeleteTemplateUseCase(repository, dispatcher) {

    var response: ChatDeleteTemplateResponse = ChatDeleteTemplateResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(ChatDeleteTemplateResponse::class.java, "error")
            }
            field = value
        }
}
