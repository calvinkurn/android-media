package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase
import com.tokopedia.topchat.stub.chattemplate.usecase.api.ChatTemplateApiStub
import javax.inject.Inject

class CreateTemplateUseCaseStub @Inject constructor(
    private val templateChatApi: ChatTemplateApiStub,
    editTemplateRepository: EditTemplateRepository,
    dispatchers: CoroutineDispatchers
): CreateTemplateUseCase(editTemplateRepository, dispatchers) {
    var delay = 0L
        set(value) {
            templateChatApi.delay = value
            field = value
        }

    var response: TemplateData = TemplateData()
        set(value) {
            templateChatApi.templateResponse = value
            field = value
        }

    var isError = false
        set(value) {
            if (value) {
                templateChatApi.error = MessageErrorException("Oops!")
            }
            field = value
        }
}