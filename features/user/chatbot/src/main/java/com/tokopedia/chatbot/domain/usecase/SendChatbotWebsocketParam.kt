package com.tokopedia.chatbot.domain.usecase

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.domain.pojo.invoiceattachment.InvoiceLinkPojo
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel

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

    fun generateParamInvoiceSendByArticle(messageId: String, invoiceLinkPojo: InvoiceLinkPojo,
                                          startTime: String, toUid: String,usedBy: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()


        data.addProperty("message_id", Integer.parseInt(messageId))

        data.addProperty("message", "Invoice")
        data.addProperty(
            "attachment_type", Integer.parseInt(
                AttachmentType
                    .Companion.TYPE_INVOICE_SEND
            )
        )


        val payload = JsonObject()
        val attributeSelected = JsonObject()

        attributeSelected.addProperty("code", invoiceLinkPojo.attributes.code)
        attributeSelected.addProperty("create_time", invoiceLinkPojo.attributes.createTime)
        attributeSelected.addProperty("id", invoiceLinkPojo.attributes.id)
        attributeSelected.addProperty("image_url", invoiceLinkPojo.attributes.imageUrl)
        attributeSelected.addProperty("status", invoiceLinkPojo.attributes.status)
        attributeSelected.addProperty("title", invoiceLinkPojo.attributes.title)
        attributeSelected.addProperty("total_amount", invoiceLinkPojo.attributes.totalAmount)
        attributeSelected.addProperty("used_by", usedBy)

        payload.addProperty("type", "Undefined")
        data.addProperty("start_time", startTime)

        payload.add("attributes", attributeSelected)
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


    fun generateParamSendQuickReplyEventArticle(
        messageId: String,
        quickReplyViewModel: QuickReplyViewModel,
        startTime: String, toUid: String, event: String, usedBy: String
    ): JsonObject {

        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", Integer.parseInt(messageId))
        data.addProperty("message", quickReplyViewModel.value)
        data.addProperty("start_time", startTime)

        data.addProperty(
            "attachment_type", Integer.parseInt(
                AttachmentType
                    .Companion.TYPE_QUICK_REPLY_SEND
            )
        )

        val payload = JsonObject()

        val selectedOption = JsonObject()

        val buttonActions = JsonObject()
        buttonActions.addProperty("text", quickReplyViewModel.text)
        buttonActions.addProperty("value", quickReplyViewModel.value)
        buttonActions.addProperty("action", quickReplyViewModel.action)

        selectedOption.add("button_actions", buttonActions)

        selectedOption.addProperty("used_by", usedBy)
        selectedOption.addProperty("event", event)

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