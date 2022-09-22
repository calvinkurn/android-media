package com.tokopedia.chatbot.domain

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.util.convertMessageIdToLong
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object ChatbotSendableWebSocketParam {

    fun getReadMessageWebSocket(messageId: String): JsonObject {
        val json = JsonObject().apply {
            addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        }
        val data = JsonObject().apply {
            addProperty("msg_id", messageId.convertMessageIdToLong())
        }
        json.add("data", data)
        return json
    }

    fun generateParamSendBubbleAction(
        messageId: String,
        chatActionBubbleViewModel: ChatActionBubbleViewModel,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject().apply {
            addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        }

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", chatActionBubbleViewModel.value)
            addProperty("start_time", startTime)
            addProperty("to_uid", toUid)
            addProperty(
                "attachment_type",
                (
                    AttachmentType
                        .Companion.TYPE_QUICK_REPLY_SEND
                    ).toIntOrZero()
            )
        }

        val payload = JsonObject()
        val selectedOption = JsonObject()
        val buttonActions = JsonObject().apply {
            addProperty("text", chatActionBubbleViewModel.text)
            addProperty("value", chatActionBubbleViewModel.value)
            addProperty("action", chatActionBubbleViewModel.action)
        }
        selectedOption.add("button_actions", buttonActions)
        payload.add("selected_option", selectedOption)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamSendMessage(
        messageId: String,
        sendMessage: String,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", sendMessage)
            addProperty("start_time", startTime)
            addProperty("to_uid", toUid)
        }
        json.add("data", data)
        return json
    }

    fun generateParamSendMessageWithReplyBubble(
        messageId: String,
        message: String,
        startTime: String,
        referredMsg: ParentReply? = null
    ): JsonObject {
        val referredMsgObj = generateReferredMsg(referredMsg)
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject().apply {
            addProperty("message", message)
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty(
                "attachment_type",
                ChatbotConstant.AttachmentType.TYPE_REPLY_BUBBLE.toIntOrZero()
            )
            addProperty("start_time", startTime)
        }
        if (referredMsgObj != null) {
            data.add("parent_reply", referredMsgObj)
        }
        json.add("data", data)
        return json
    }

    private fun generateReferredMsg(referredMsg: ParentReply?): JsonElement? {
        if (referredMsg == null) {
            return null
        }
        val request = JsonObject().apply {
            addProperty("sender_id", referredMsg.senderId.toLongOrZero())
            addProperty("reply_time", referredMsg.replyTime.toLongOrZero())
            addProperty("main_text", referredMsg.mainText)
            addProperty("name", referredMsg.name)
        }
        return request
    }

    fun generateParamSendInvoice(
        messageId: String,
        invoiceLinkPojo: InvoiceLinkPojo,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val invoiceAttribute = invoiceLinkPojo.attributes
        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", invoiceAttribute.code)
            addProperty("start_time", startTime)
            addProperty("to_uid", toUid)
            addProperty(
                "attachment_type",
                (
                    AttachmentType
                        .Companion.TYPE_INVOICE_SEND
                    ).toIntOrZero()
            )
        }

        // TODO review this - need to check for exception
        val payload =
            GsonBuilder().create().toJsonTree(invoiceLinkPojo, InvoiceLinkPojo::class.java)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamInvoiceSendByArticle(
        messageId: String,
        invoiceLinkPojo: InvoiceLinkPojo,
        startTime: String,
        usedBy: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", "Invoice")
            addProperty(
                "attachment_type",
                (
                    AttachmentType
                        .Companion.TYPE_INVOICE_SEND
                    ).toIntOrZero()
            )
        }

        val payload = JsonObject()
        val attributeSelected = JsonObject().apply {
            addProperty("code", invoiceLinkPojo.attributes.code)
            addProperty("create_time", invoiceLinkPojo.attributes.createTime)
            addProperty("id", invoiceLinkPojo.attributes.id)
            addProperty("image_url", invoiceLinkPojo.attributes.imageUrl)
            addProperty("status", invoiceLinkPojo.attributes.status)
            addProperty("title", invoiceLinkPojo.attributes.title)
            addProperty("total_amount", invoiceLinkPojo.attributes.totalAmount)
            addProperty("used_by", usedBy)
        }

        payload.addProperty("type", "Undefined")
        data.addProperty("start_time", startTime)
        payload.add("attributes", attributeSelected)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamSendQuickReply(
        messageId: String,
        quickReplyViewModel: QuickReplyViewModel,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", quickReplyViewModel.value)
            addProperty("start_time", startTime)
            addProperty("to_uid", toUid)
            addProperty(
                "attachment_type",
                (
                    AttachmentType
                        .Companion.TYPE_QUICK_REPLY_SEND
                    ).toIntOrZero()
            )
        }

        val payload = JsonObject()
        val selectedOption = JsonObject()

        val quickReplies = JsonObject().apply {
            addProperty("text", quickReplyViewModel.text)
            addProperty("value", quickReplyViewModel.value)
            addProperty("action", quickReplyViewModel.action)
        }

        selectedOption.add("quick_replies", quickReplies)
        payload.add("selected_option", selectedOption)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamSendQuickReplyEventArticle(
        messageId: String,
        quickReplyViewModel: QuickReplyViewModel,
        startTime: String,
        event: String,
        usedBy: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", quickReplyViewModel.value)
            addProperty("start_time", startTime)
            addProperty(
                "attachment_type",
                (
                    AttachmentType
                        .Companion.TYPE_QUICK_REPLY_SEND
                    ).toIntOrZero()
            )
        }

        val payload = JsonObject()
        val buttonActions = JsonObject().apply {
            addProperty("text", quickReplyViewModel.text)
            addProperty("value", quickReplyViewModel.value)
            addProperty("action", quickReplyViewModel.action)
        }
        val selectedOption = JsonObject().apply {
            add("button_actions", buttonActions)
            addProperty("used_by", usedBy)
            addProperty("event", event)
        }

        payload.add("selected_option", selectedOption)
        data.add("payload", payload)
        json.add("data", data)
        return json
    }

    fun generateParamUploadSecureSendImage(
        messageId: String,
        path: String,
        startTime: String,
        name: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("from", name)
            addProperty("from_user_name", name)
            addProperty("message", "Uploaded Image")
            addProperty("start_time", startTime)
            addProperty("file_path", path)
            addProperty(
                "attachment_type",
                ChatbotConstant.AttachmentType.TYPE_SECURE_IMAGE_UPLOAD.toIntOrZero()
            )
        }
        json.add("data", data)
        return json
    }

    fun generateParamSendImage(
        messageId: String,
        path: String,
        imageObj: String,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)
        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", "Uploaded Image")
            addProperty("start_time", startTime)
            addProperty("to_uid", toUid)
            addProperty("file_path", path)
            addProperty("image_obj", imageObj)
            addProperty("attachment_type", AttachmentType.Companion.TYPE_IMAGE_UPLOAD.toIntOrZero())
        }
        json.add("data", data)
        return json
    }

    fun getReadMessage(messageId: String): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_READ_MESSAGE)
        val data = JsonObject().apply {
            addProperty("msg_id", messageId.convertMessageIdToLong())
            addProperty("no_update", true)
        }

        json.add("data", data)
        return json
    }
}
