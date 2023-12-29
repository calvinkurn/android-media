package com.tokopedia.topchat.stub.chatroom.usecase

import com.google.gson.JsonArray
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

    private val defaultResponsePath = "product_preattach/success_get_pre_attach_payload.json"
    private val preAttachDoubleProductResponsePath =
        "product_preattach/success_get_pre_attach_payload_2.json"
    private val preAttachTripleProductResponsePath =
        "product_preattach/success_get_pre_attach_payload_3.json"

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
            alterPreAttachPayload(list, productId)
        }
    }

    fun generate2PreAttachPayload(): PreAttachPayloadResponse {
        return alterResponseOf(preAttachDoubleProductResponsePath) {
        }
    }

    fun generate3PreAttachPayload(): PreAttachPayloadResponse {
        return alterResponseOf(preAttachTripleProductResponsePath) {
        }
    }

    private fun alterPreAttachPayload(list: JsonArray, productId: String? = null) {
        for (i in 0 until list.size()) {
            val stringId: String = productId ?: i.toString()
            list[i].asJsonObject.addProperty(id, stringId)
            val attrs = list[i].asJsonObject.get(attributes).asString
            val attrObj = fromJson<JsonObject>(attrs)
            attrObj.addProperty(product_id, stringId)
            list[i].asJsonObject.addProperty(attributes, attrObj.toString())
        }
    }

    fun delayResponseIndefinitely() {
        repository.delayMs = Int.MAX_VALUE.toLong()
    }

    fun setError() {
        repository.createErrorMapResult(response::class.java, "")
    }

    companion object {
        private const val chatPreAttachPayload = "chatPreAttachPayload"
        private const val list = "list"
        private const val id = "id"
        private const val attributes = "attributes"
        private const val product_id = "product_id"
    }
}
