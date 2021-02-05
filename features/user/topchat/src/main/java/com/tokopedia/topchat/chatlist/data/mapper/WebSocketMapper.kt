package com.tokopedia.topchat.chatlist.data.mapper

import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import com.tokopedia.topchat.chatlist.model.IncomingChatWebSocketModel
import com.tokopedia.topchat.chatlist.model.IncomingTypingWebSocketModel
import com.tokopedia.topchat.chatlist.pojo.ItemChatAttributesContactPojo
import com.tokopedia.websocket.WebSocketResponse

object WebSocketMapper {

    fun mapToIncomingChat(response: WebSocketResponse): IncomingChatWebSocketModel {
        val json = response.jsonObject
        val responseData = Gson().fromJson(json, WebSocketResponseData::class.java)
        val msgId = responseData.msgId.toString()
        val message = responseData.message.censoredReply.trim().toEmptyStringIfNull()
        val time = responseData.message.timeStampUnix.toEmptyStringIfNull()

        val contact = ItemChatAttributesContactPojo(
                responseData?.fromUid.toString(),
                responseData?.fromRole.toString(),
                "",
                responseData?.from.toString(),
                0,
                responseData?.fromRole.toString(),
                responseData?.imageUri.toString(),
                responseData.isAutoReply
        )
        return IncomingChatWebSocketModel(msgId, message, time, contact)
    }

    fun mapToIncomingTypeState(response: WebSocketResponse, isTyping: Boolean): IncomingTypingWebSocketModel {
        val json = response.jsonObject
        val responseData = Gson().fromJson(json, WebSocketResponseData::class.java)
        val msgId = responseData?.msgId.toString()

        val contact = ItemChatAttributesContactPojo(
                responseData?.fromUid.toString(),
                responseData?.fromRole.toString(),
                "",
                responseData?.from.toString(),
                0,
                responseData?.fromRole.toString(),
                ""
        )

        return IncomingTypingWebSocketModel(msgId, isTyping, contact)
    }

}