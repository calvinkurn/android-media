package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.domain.pojo.ChatToggleTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.usecase.ToggleTemplateUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class ToggleTemplateUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): ToggleTemplateUseCase(repository, dispatcher) {

    var response: ChatToggleTemplateResponse = ChatToggleTemplateResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(ChatToggleTemplateResponse::class.java, "error")
            }
            field = value
        }
}
