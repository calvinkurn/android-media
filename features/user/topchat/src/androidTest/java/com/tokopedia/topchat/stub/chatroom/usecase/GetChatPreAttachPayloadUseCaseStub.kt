package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.stub.common.GraphqlRepositoryStub

class GetChatPreAttachPayloadUseCaseStub(
        private val repository: GraphqlRepositoryStub,
        dispatchers: CoroutineDispatchers
) : GetChatPreAttachPayloadUseCase(repository, dispatchers) {

    private val defaultResponsePath = "success_get_pre_attach_payload.json"

    var response: PreAttachPayloadResponse = PreAttachPayloadResponse()
        set(value) {
            repository.createMapResult(response::class.java, value)
            field = value
        }

    fun generatePreAttachPayload(
            productId: String
    ): PreAttachPayloadResponse {
        return alterResponseOf(defaultResponsePath) {
            it.getAsJsonObject(chatPreAttachPayload)
                    .getAsJsonArray(list)[0].asJsonObject
                    .addProperty(id, productId)
        }
    }

    companion object {
        private const val chatPreAttachPayload = "chatPreAttachPayload"
        private const val list = "list"
        private const val id = "id"
    }
}