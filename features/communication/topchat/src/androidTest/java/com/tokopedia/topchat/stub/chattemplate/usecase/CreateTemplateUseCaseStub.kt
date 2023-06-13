package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatAddTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class CreateTemplateUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
) : CreateTemplateUseCase(repository, dispatcher) {

    var response: ChatAddTemplateResponse = ChatAddTemplateResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(ChatAddTemplateResponse::class.java, "error")
            }
            field = value
        }
}
