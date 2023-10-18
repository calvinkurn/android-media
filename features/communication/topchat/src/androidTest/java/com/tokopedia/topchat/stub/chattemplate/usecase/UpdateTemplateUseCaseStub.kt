package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatUpdateTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.usecase.UpdateTemplateUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class UpdateTemplateUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): UpdateTemplateUseCase(repository, dispatcher) {

    var response: ChatUpdateTemplateResponse = ChatUpdateTemplateResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(ChatUpdateTemplateResponse::class.java, "error")
            }
            field = value
        }
}
