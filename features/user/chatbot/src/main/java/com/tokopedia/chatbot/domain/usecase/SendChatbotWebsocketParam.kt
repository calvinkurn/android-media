package com.tokopedia.chatbot.domain.usecase

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo

/**
 * @author by nisie on 19/12/18.
 */
object SendChatbotWebsocketParam {

    fun generateParamSendInvoice(messageId: String, invoiceLinkPojo: InvoiceLinkPojo,
                                 startTime: String, toUid: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        val invoiceAttribute = invoiceLinkPojo.attributes
        data.addProperty("message_id", Integer.parseInt(messageId))
        data.addProperty("message", invoiceAttribute.code)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty("attachment_type", Integer.parseInt(AttachmentType
                .Companion.TYPE_INVOICE_SEND))
        val payload = GsonBuilder().create().toJsonTree(invoiceLinkPojo, InvoiceLinkPojo::class.java)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamSendQuickReply(messageId: String,
                                    quickReplyViewModel: QuickReplyViewModel,
                                    startTime: String, toUid: String): JsonObject {

        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", Integer.parseInt(messageId))
        data.addProperty("message", quickReplyViewModel.value)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty("attachment_type", Integer.parseInt(AttachmentType
                .Companion.TYPE_QUICK_REPLY_SEND))

        val payload = JsonObject()

        val selectedOption = JsonObject()

        val quickReplies = JsonObject()
        quickReplies.addProperty("text", quickReplyViewModel.text)
        quickReplies.addProperty("value", quickReplyViewModel.value)
        quickReplies.addProperty("action", quickReplyViewModel.action)

        selectedOption.add("quick_replies", quickReplies)

        payload.add("selected_option", selectedOption)

        data.add("payload", payload)

        json.add("data", data)
        return json
    }

    fun generateParamSendBubbleAction(messageId: String,
                                      chatActionBubbleViewModel: ChatActionBubbleViewModel,
                                      startTime: String, toUid: String): JsonObject {

        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", Integer.parseInt(messageId))
        data.addProperty("message", chatActionBubbleViewModel.value)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty("attachment_type", Integer.parseInt(AttachmentType
                .Companion.TYPE_QUICK_REPLY_SEND))

        val payload = JsonObject()

        val selectedOption = JsonObject()

        val buttonActions = JsonObject()
        buttonActions.addProperty("text", chatActionBubbleViewModel.text)
        buttonActions.addProperty("value", chatActionBubbleViewModel.value)
        buttonActions.addProperty("action", chatActionBubbleViewModel.action)

        selectedOption.add("button_actions", buttonActions)

        payload.add("selected_option", selectedOption)

        data.add("payload", payload)

        json.add("data", data)
        return json
    }


}