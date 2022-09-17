package com.tokopedia.chatbot.domain.usecase

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.util.convertMessageIdToLong
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * @author by nisie on 19/12/18.
 */
object SendChatbotWebsocketParam {

    fun generateParamSendInvoice(
        messageId: String,
        invoiceLinkPojo: InvoiceLinkPojo,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        val invoiceAttribute = invoiceLinkPojo.attributes
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", invoiceAttribute.code)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty(
            "attachment_type",
            (
                AttachmentType
                    .Companion.TYPE_INVOICE_SEND
                ).toIntOrZero()
        )
        val payload = GsonBuilder().create().toJsonTree(invoiceLinkPojo, InvoiceLinkPojo::class.java)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamInvoiceSendByArticle(
        messageId: String,
        invoiceLinkPojo: InvoiceLinkPojo,
        startTime: String,
        toUid: String,
        usedBy: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()

        data.addProperty("message_id", messageId.convertMessageIdToLong())

        data.addProperty("message", "Invoice")
        data.addProperty(
            "attachment_type",
            (
                AttachmentType
                    .Companion.TYPE_INVOICE_SEND
                ).toIntOrZero()
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

    fun generateParamSendQuickReply(
        messageId: String,
        quickReplyUiModel: QuickReplyUiModel,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", quickReplyUiModel.value)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty(
            "attachment_type",
            (
                AttachmentType
                    .Companion.TYPE_QUICK_REPLY_SEND
                ).toIntOrZero()
        )

        val payload = JsonObject()

        val selectedOption = JsonObject()

        val quickReplies = JsonObject()
        quickReplies.addProperty("text", quickReplyUiModel.text)
        quickReplies.addProperty("value", quickReplyUiModel.value)
        quickReplies.addProperty("action", quickReplyUiModel.action)

        selectedOption.add("quick_replies", quickReplies)

        payload.add("selected_option", selectedOption)

        data.add("payload", payload)

        json.add("data", data)
        return json
    }

    fun generateParamSendQuickReplyEventArticle(
        messageId: String,
        quickReplyUiModel: QuickReplyUiModel,
        startTime: String,
        toUid: String,
        event: String,
        usedBy: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", quickReplyUiModel.value)
        data.addProperty("start_time", startTime)

        data.addProperty(
            "attachment_type",
            (
                AttachmentType
                    .Companion.TYPE_QUICK_REPLY_SEND
                ).toIntOrZero()
        )

        val payload = JsonObject()

        val selectedOption = JsonObject()

        val buttonActions = JsonObject()
        buttonActions.addProperty("text", quickReplyUiModel.text)
        buttonActions.addProperty("value", quickReplyUiModel.value)
        buttonActions.addProperty("action", quickReplyUiModel.action)

        selectedOption.add("button_actions", buttonActions)

        selectedOption.addProperty("used_by", usedBy)
        selectedOption.addProperty("event", event)

        payload.add("selected_option", selectedOption)

        data.add("payload", payload)

        json.add("data", data)
        return json
    }

    fun generateParamSendBubbleAction(
        messageId: String,
        chatActionBubbleUiModel: ChatActionBubbleUiModel,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", chatActionBubbleUiModel.value)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)

        data.addProperty(
            "attachment_type",
            (
                AttachmentType
                    .Companion.TYPE_QUICK_REPLY_SEND
                ).toIntOrZero()
        )

        val payload = JsonObject()

        val selectedOption = JsonObject()

        val buttonActions = JsonObject()
        buttonActions.addProperty("text", chatActionBubbleUiModel.text)
        buttonActions.addProperty("value", chatActionBubbleUiModel.value)
        buttonActions.addProperty("action", chatActionBubbleUiModel.action)

        selectedOption.add("button_actions", buttonActions)

        payload.add("selected_option", selectedOption)

        data.add("payload", payload)

        json.add("data", data)
        return json
    }

    fun getReadMessage(messageId: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject()
        data.addProperty("msg_id", messageId.convertMessageIdToLong())
        data.addProperty("no_update", true)
        json.add("data", data)
        return json
    }

    fun generateParamSendMessage(
        messageId: String,
        sendMessage: String,
        startTime: String,
        toUid:
            String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", sendMessage)
        data.addProperty("start_time", startTime)
        data.addProperty("to_uid", toUid)
        json.add("data", data)
        return json
    }
}
