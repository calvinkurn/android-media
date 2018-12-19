package com.tokopedia.chatbot.domain.usecase

import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo

/**
 * @author by nisie on 19/12/18.
 */
object SendChatbotWebsocketParam {

    fun generateParamSendInvoice(messageId: String, invoiceLinkPojo: InvoiceLinkPojo, startTime: String): JsonObject {
        val json = JsonObject()
        return json
    }
}