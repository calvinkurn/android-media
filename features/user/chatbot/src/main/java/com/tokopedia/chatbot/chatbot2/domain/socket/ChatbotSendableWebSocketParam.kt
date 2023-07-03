package com.tokopedia.chatbot.chatbot2.domain.socket

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.util.convertMessageIdToLong
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import org.json.JSONException

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
        chatActionBubbleViewModel: ChatActionBubbleUiModel,
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
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
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
            addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
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
            addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
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

        val payload =
            GsonBuilder().create().toJsonTree(invoiceLinkPojo, InvoiceLinkPojo::class.java)
        data.add("payload", payload)
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
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
            addProperty("color", invoiceLinkPojo.attributes.color)
        }

        payload.addProperty("type", "Undefined")
        data.addProperty("start_time", startTime)
        payload.add("attributes", attributeSelected)
        data.add("payload", payload)
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)

        json.add("data", data)
        return json
    }

    fun generateParamSendQuickReply(
        messageId: String,
        quickReplyViewModel: QuickReplyUiModel,
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

        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)

        json.add("data", data)
        return json
    }

    fun generateParamSendQuickReplyEventArticle(
        messageId: String,
        quickReplyViewModel: QuickReplyUiModel,
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

        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)

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
            addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        }
        json.add("data", data)
        return json
    }

    fun generateParamSendVideoAttachment(
        filePath: String,
        startTime: String,
        messageId: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val data = JsonObject()
        data.addProperty("message_id", messageId.convertMessageIdToLong())
        data.addProperty("message", "Uploaded Video")
        data.addProperty(
            "attachment_type",
            (
                ChatbotConstant.AttachmentType.TYPE_VIDEO_UPLOAD
                ).toIntOrZero()
        )
        data.addProperty("file_path", filePath)
        data.addProperty("start_time", startTime)
        data.addProperty("source", ChatbotConstant.SOURCE_CHATBOT)

        json.add("data", data)
        return json
    }

    fun generateParamDynamicAttachmentText(
        messageId: String,
        bubbleUiModel: ChatActionBubbleUiModel,
        startTime: String,
        toUid: String
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val dynamicContent = generateDynamicContent(bubbleUiModel)

        val attribute = JsonObject().apply {
            addProperty(
                "content_code",
                ChatbotConstant.DynamicAttachment.DYNAMIC_TEXT_SEND
            )
            addProperty("dynamic_content", dynamicContent)
            addProperty("user_id", toUid.toLongOrZero())
        }

        val payload = JsonObject().apply {
            add("attribute", attribute)
            addProperty("is_log_history", true)
        }

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", bubbleUiModel.text)
            addProperty("attachment_type", ChatbotConstant.DynamicAttachment.DYNAMIC_ATTACHMENT.toIntOrZero())
            add("payload", payload)
            addProperty("start_time", startTime)
            addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        }

        json.add("data", data)

        return json
    }

    private fun generateDynamicContent(bubbleUiModel: ChatActionBubbleUiModel): String {
        val buttonActionContent = JsonObject().apply {
            addProperty("action", bubbleUiModel.action)
            addProperty("text", bubbleUiModel.text)
            addProperty("value", bubbleUiModel.value)
        }
        val content = JsonObject().apply {
            add("button_action", buttonActionContent)
        }
        return try {
            Gson().toJson(content)
        } catch (e: JSONException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            ""
        }
    }

    fun generateParamDynamicAttachment108(
        reasonCodeList: List<Long>,
        reasonText: String,
        messageId: String,
        toUid: String,
        startTime: String,
        helpfulQuestion: DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion?,
        index: Int = 0,
        isSubmitAfterOpenForm: Boolean
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val dynamicContent = generateDynamicContent108(reasonCodeList, reasonText, helpfulQuestion, index, isSubmitAfterOpenForm)

        val attribute = JsonObject().apply {
            addProperty(
                "content_code",
                ChatbotConstant.DynamicAttachment.DYNAMIC_REJECT_REASON_SEND
            )
            addProperty("dynamic_content", dynamicContent)
            addProperty("user_id", toUid.toLongOrZero())
        }

        val payload = JsonObject().apply {
            add("attribute", attribute)
            addProperty("is_log_history", true)
        }

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            if ((helpfulQuestion?.quickReplies?.size ?: 0) >= (index + 1)) {
                addProperty("message", helpfulQuestion?.quickReplies?.get(index)?.message)
            }
            addProperty("attachment_type", ChatbotConstant.DynamicAttachment.DYNAMIC_ATTACHMENT.toIntOrZero())
            add("payload", payload)
            addProperty("start_time", startTime)
            addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        }

        json.add("data", data)

        return json
    }

    private fun generateDynamicContent108(
        reasonCodeList: List<Long>,
        reasonText: String,
        helpfulQuestion: DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion?,
        index: Int,
        isSubmitAfterOpenForm: Boolean
    ): String {
        val newQuickRepliesBody = JsonObject().apply {
            if ((helpfulQuestion?.newQuickRepliesList?.size ?: 0) >= (index + 1)) {
                helpfulQuestion?.newQuickRepliesList?.get(index)?.let {
                    addProperty("action", it.action)
                    addProperty("text", it.text)
                    addProperty("value", it.value)
                }
            }
        }

        val newQuickRepliesArray = JsonArray()
        newQuickRepliesArray.add(newQuickRepliesBody)

        val newQuickReplies = JsonObject().apply {
            add("new_quick_replies", newQuickRepliesArray)
        }

        val reasonsJsonArray = JsonArray()
        reasonCodeList.forEach {
            reasonsJsonArray.add(it)
        }

        val feedbackFormBody = JsonObject().apply {
            addProperty("reason", reasonText)
            add("reason_chip_code_list", reasonsJsonArray)
        }

        val helpfulQuestions = JsonObject().apply {
            add("helpful_question", newQuickReplies)
            add("feedback_form", feedbackFormBody)
            addProperty("is_submit_after_open_form", isSubmitAfterOpenForm)
        }

        val content = JsonObject().apply {
            add("helpful_question_feedback_form", helpfulQuestions)
        }

        return try {
            Gson().toJson(content)
        } catch (e: JSONException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            ""
        }
    }

    fun generateParamDynamicAttachment108ForAcknowledgement(
        messageId: String,
        toUid: String,
        model: QuickReplyUiModel,
        startTime: String,
        index: Int = 1,
        isSubmitAfterOpenForm: Boolean
    ): JsonObject {
        val json = JsonObject()
        json.addProperty("code", WebsocketEvent.Event.EVENT_TOPCHAT_REPLY_MESSAGE)

        val dynamicContent = generateDynamicContent108ForYa(model, index, isSubmitAfterOpenForm)

        val attribute = JsonObject().apply {
            addProperty(
                "content_code",
                ChatbotConstant.DynamicAttachment.DYNAMIC_REJECT_REASON_SEND
            )
            addProperty("dynamic_content", dynamicContent)
            addProperty("user_id", toUid.toLongOrZero())
        }

        val payload = JsonObject().apply {
            add("attribute", attribute)
            addProperty("is_log_history", true)
        }

        val data = JsonObject().apply {
            addProperty("message_id", messageId.convertMessageIdToLong())
            addProperty("message", model.value)
            addProperty("attachment_type", ChatbotConstant.DynamicAttachment.DYNAMIC_ATTACHMENT.toIntOrZero())
            add("payload", payload)
            addProperty("start_time", startTime)
            addProperty("source", ChatbotConstant.SOURCE_CHATBOT)
        }

        json.add("data", data)

        return json
    }

    private fun generateDynamicContent108ForYa(
        helpfulQuestion: QuickReplyUiModel,
        index: Int,
        isSubmitAfterOpenForm: Boolean
    ): String {
        val newQuickRepliesBody = JsonObject().apply {
            addProperty("action", helpfulQuestion.action)
            addProperty("text", helpfulQuestion.text)
            addProperty("value", helpfulQuestion.value)
        }

        val newQuickRepliesArray = JsonArray()
        newQuickRepliesArray.add(newQuickRepliesBody)

        val newQuickReplies = JsonObject().apply {
            add("new_quick_replies", newQuickRepliesArray)
        }

        val helpfulQuestions = JsonObject().apply {
            add("helpful_question", newQuickReplies)
            add("feedback_form", JsonObject())
            addProperty("is_submit_after_open_form", isSubmitAfterOpenForm)
        }

        val content = JsonObject().apply {
            add("helpful_question_feedback_form", helpfulQuestions)
        }

        return try {
            Gson().toJson(content)
        } catch (e: JSONException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            ""
        }
    }
}
