package com.tokopedia.topchat.stub.chattemplate.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.usecase.GetTemplateUseCase
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub
import javax.inject.Inject

class GetTemplateUseCaseStub @Inject constructor(
    private val repository: GraphqlRepositoryStub,
    dispatcher: CoroutineDispatchers
): GetTemplateUseCase(repository, dispatcher) {

    var response: GetChatTemplateResponse = GetChatTemplateResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    var isError: Boolean = false
        set(value) {
            if (value) {
                repository.createErrorMapResult(GetChatTemplateResponse::class.java, "error")
            }
            field = value
        }

    var successGetTemplateResponseBuyer = AndroidFileUtil.parse<GetChatTemplateResponse>(
        "template/success_get_template.json",
        GetChatTemplateResponse::class.java
    )

    fun getTemplateResponseBuyer(isEnabled: Boolean): GetChatTemplateResponse {
        val templateResponse = successGetTemplateResponseBuyer
        templateResponse.chatTemplatesAll.buyerTemplate.isEnable = isEnabled
        return templateResponse
    }
}
