package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatroom.domain.pojo.preattach.PreAttachPayloadResponse
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatPreAttachPayloadUseCase
import com.tokopedia.topchat.common.alterResponseOf
import com.tokopedia.topchat.common.fromJson
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
            val list = it.getAsJsonObject(chatPreAttachPayload)
                    .getAsJsonArray(list)
            list[0].asJsonObject.addProperty(id, productId)
            val attrs = list[0].asJsonObject.get(attributes).asString
            val attrObj = fromJson<JsonObject>(attrs)
            attrObj.addProperty(product_id, productId)
            list[0].asJsonObject.addProperty(attributes, attrObj.toString())
        }
    }

    fun generate3PreAttachPayload(
            productId: String
    ): PreAttachPayloadResponse {
        return alterResponseOf(defaultResponsePath) { response ->
            val list = response.getAsJsonObject(chatPreAttachPayload)
                    .getAsJsonArray(list)
            list.add(list[0])
            list.add(list[0])
            list.forEach {
                it.asJsonObject.addProperty(id, productId)
            }
        }
    }

    fun delayResponseIndefinitely() {
        repository.delayMs = Int.MAX_VALUE.toLong()
    }

    companion object {
        private const val chatPreAttachPayload = "chatPreAttachPayload"
        private const val list = "list"
        private const val id = "id"
        private const val attributes = "attributes"
        private const val product_id = "product_id"
    }
}