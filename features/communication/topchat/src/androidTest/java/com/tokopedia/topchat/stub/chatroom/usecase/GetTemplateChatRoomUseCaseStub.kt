package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.stub.chatroom.usecase.api.ChatRoomApiStub
import javax.inject.Inject

class GetTemplateChatRoomUseCaseStub @Inject constructor(
    private val apiStub: ChatRoomApiStub,
    mapper: GetTemplateChatRoomMapper,
    dispatchers: CoroutineDispatchers
) : GetTemplateChatRoomUseCase(apiStub, mapper, dispatchers) {

    var response: TemplateData = TemplateData()
        set(value) {
            apiStub.templateResponse = value
            field = value
        }

    fun setResponse(response: TemplateData, delayMillis: Long) {
        this.response = response
        apiStub.delay = delayMillis
    }
}
