package com.tokopedia.chatbot.chatbot2.domain.mapper

import android.text.TextUtils
import android.util.Log
import androidx.annotation.NonNull
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_RATING
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICES_SELECTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY_SEND
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CSAT_OPTIONS
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_HELPFULL_QUESTION
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_REPLY_BUBBLE
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_SECURE_IMAGE_UPLOAD
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_STICKY_BUTTON
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_VIDEO_UPLOAD
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceSentPojo
import com.tokopedia.chatbot.chatbot2.data.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.chatbot2.data.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.chatbot2.data.imageupload.ChatbotImageUploadAttributes
import com.tokopedia.chatbot.chatbot2.data.invoicelist.websocket.InvoicesSelectionPojo
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.stickyactionbutton.StickyActionButtonPojo
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.ChatbotVideoUploadAttributes
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.stickyactionbutton.StickyActionButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */

class ChatBotWebSocketMessageMapper @Inject constructor() : WebsocketMessageMapper() {

    override fun map(pojo: ChatSocketPojo): Visitable<*> {
        return if (pojo.showRating || pojo.ratingStatus != 0) {
            convertToChatRating(pojo)
        } else {
            super.map(pojo)
        }
    }

    override fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment?.type) {
            TYPE_QUICK_REPLY -> convertToQuickReplyModel(pojo, jsonAttributes)
            TYPE_INVOICES_SELECTION -> convertToInvoiceSelection(pojo, jsonAttributes)
            TYPE_CHAT_BALLOON_ACTION -> convertToChatActionSelectionBubbleModel(pojo, jsonAttributes)
            TYPE_QUICK_REPLY_SEND -> convertToMessageViewModel(pojo)
            TYPE_HELPFULL_QUESTION -> convertToHelpQuestionViewModel(pojo)
            TYPE_CSAT_OPTIONS -> convertToCsatOptionsViewModel(pojo)
            TYPE_STICKY_BUTTON -> convertToStickedButtonActionsViewModel(pojo)
            TYPE_SECURE_IMAGE_UPLOAD -> convertToImageUpload(pojo, jsonAttributes)
            TYPE_REPLY_BUBBLE -> convertToReplyBubble(pojo, jsonAttributes)
            AttachmentType.Companion.TYPE_INVOICE_SEND -> convertToSendInvoice(pojo, jsonAttributes)
            TYPE_VIDEO_UPLOAD -> convertToVideoUpload(pojo, jsonAttributes)
            else -> super.mapAttachmentMessage(pojo, jsonAttributes)
        }
    }

    private fun convertToSendInvoice(pojo: ChatSocketPojo, jsonAttributes: JsonObject): com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel {
        val invoiceSentPojo = GsonBuilder().create().fromJson(
            jsonAttributes,
            InvoiceSentPojo::class.java
        )
        return com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel.Builder()
            .withResponseFromWs(pojo)
            .withInvoiceAttributesResponse(invoiceSentPojo.invoiceLink)
            .withNeedSync(false)
            .build()
    }

    private fun convertToImageUpload(@NonNull pojo: ChatSocketPojo, jsonAttribute: JsonObject):
        ImageUploadUiModel {
        val pojoAttribute = GsonBuilder().create().fromJson(
            jsonAttribute,
            ChatbotImageUploadAttributes::class.java
        )

        return ImageUploadUiModel.Builder()
            .withResponseFromWs(pojo)
            .withImageUrl(pojoAttribute.imageUrlSecure)
            .withImageUrlThumbnail(pojoAttribute.thumbnail)
            .build()
    }

    private fun convertToVideoUpload(@NonNull pojo: ChatSocketPojo, jsonAttribute: JsonObject):
        VideoUploadUiModel {
        val pojoAttribute = GsonBuilder().create().fromJson(
            jsonAttribute,
            ChatbotVideoUploadAttributes::class.java
        )

        return VideoUploadUiModel.Builder()
            .withResponseFromWs(pojo)
            .withVideoUrl(pojoAttribute.videoUrl)
            .build()
    }

    private fun convertToStickedButtonActionsViewModel(pojo: ChatSocketPojo): Visitable<*> {
        val stickyActionButtonPojo = GsonBuilder().create()
            .fromJson(
                pojo.attachment?.attributes,
                StickyActionButtonPojo::class.java
            )
        return StickyActionButtonUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.attachment?.id ?: "",
            pojo.attachment?.type ?: "",
            pojo.message.timeStampUnixNano,
            pojo.message.censoredReply,
            stickyActionButtonPojo.stickedButtonActions,
            pojo.source
        )
    }

    private fun convertToHelpQuestionViewModel(pojo: ChatSocketPojo): Visitable<*> {
        val helpFullQuestionPojo = GsonBuilder().create()
            .fromJson<HelpFullQuestionPojo>(
                pojo.attachment?.attributes,
                HelpFullQuestionPojo::class.java
            )
        return HelpFullQuestionsUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.attachment?.id ?: "",
            pojo.attachment?.type ?: "",
            pojo.message.timeStampUnixNano,
            pojo.message.censoredReply,
            helpFullQuestionPojo.helpfulQuestion,
            pojo.source
        )
    }

    private fun convertToCsatOptionsViewModel(pojo: ChatSocketPojo): Visitable<*> {
        val csatAttributesPojo = GsonBuilder().create()
            .fromJson<CsatAttributesPojo>(
                pojo.attachment?.attributes,
                CsatAttributesPojo::class.java
            )
        return CsatOptionsUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.attachment?.id ?: "",
            pojo.attachment?.type ?: "",
            pojo.message.timeStampUnixNano,
            pojo.message.censoredReply,
            csatAttributesPojo.csat,
            pojo.source
        )
    }

    private fun convertToChatRating(pojo: ChatSocketPojo): Visitable<*> {
        val quickReplyPojo = GsonBuilder().create()
            .fromJson<QuickReplyAttachmentAttributes>(
                pojo.attachment?.attributes,
                QuickReplyAttachmentAttributes::class.java
            )
        return ChatRatingUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.message.censoredReply,
            pojo.attachment?.id ?: "",
            TYPE_CHAT_RATING,
            pojo.message.timeStampUnixNano,
            pojo.ratingStatus,
            pojo.message.timeStampUnixNano.toLong(),
            convertToQuickReplyList(quickReplyPojo)
        )
    }

    private fun convertToInvoiceSelection(
        pojo: ChatSocketPojo,
        jsonAttribute: JsonObject
    ): AttachInvoiceSelectionUiModel {
        val invoiceListKey = "invoice_list"
        val jsonObject = jsonAttribute.getAsJsonObject(invoiceListKey)

        val invoicesSelectionPojo = GsonBuilder().create().fromJson<InvoicesSelectionPojo>(jsonObject, InvoicesSelectionPojo::class.java)
        val invoiceList = invoicesSelectionPojo.invoices

        val list = ArrayList<AttachInvoiceSingleUiModel>()

        for (invoice in invoiceList) {
            val attributes = invoice.attributes
            val attachInvoice = AttachInvoiceSingleUiModel(
                invoice.type,
                invoice.typeId,
                attributes.code,
                attributes.createdTime,
                attributes.description,
                attributes.url,
                attributes.id,
                attributes.imageUrl,
                attributes.status,
                attributes.statusId,
                attributes.title,
                attributes.amount,
                attributes.color
            )
            list.add(attachInvoice)
        }

        return AttachInvoiceSelectionUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.attachment!!.id,
            pojo.attachment!!.type,
            pojo.message.timeStampUnixNano,
            list,
            pojo.message.censoredReply,
            pojo.source
        )
    }

    private fun convertToChatActionSelectionBubbleModel(pojo: ChatSocketPojo, jsonAttribute: JsonObject): ChatActionSelectionBubbleUiModel {
        val pojoAttribute = GsonBuilder().create().fromJson(jsonAttribute, com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes::class.java)
        val quickReplyPojo = GsonBuilder().create()
            .fromJson<QuickReplyAttachmentAttributes>(
                jsonAttribute,
                QuickReplyAttachmentAttributes::class.java
            )

        Log.d("FATAL", "convertToChatActionSelectionBubbleModel: Converting to Ui Model")
        return ChatActionSelectionBubbleUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.attachment!!.id,
            pojo.attachment!!.type,
            pojo.message.timeStampUnixNano,
            pojo.message.censoredReply,
            convertToChatActionBubbleViewModelList(pojoAttribute),
            convertToQuickReplyList(quickReplyPojo)
        )
    }

    private fun convertToChatActionBubbleViewModelList(
        pojo: com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
    ): List<ChatActionBubbleUiModel> {
        val result = ArrayList<ChatActionBubbleUiModel>()
        for (item in pojo.chatActions) {
            result.add(ChatActionBubbleUiModel(item.text, item.value, item.action, hexColor = item.hexColor, iconUrl = item.iconUrl))
        }
        return result
    }

    private fun convertToQuickReplyModel(pojo: ChatSocketPojo, jsonAttribute: JsonObject): QuickReplyListUiModel {
        val pojoAttribute = GsonBuilder().create()
            .fromJson<QuickReplyAttachmentAttributes>(
                jsonAttribute,
                QuickReplyAttachmentAttributes::class.java
            )
        return QuickReplyListUiModel(
            pojo.msgId.toString(),
            pojo.fromUid,
            pojo.from,
            pojo.fromRole,
            pojo.message.censoredReply,
            pojo.attachment!!.id,
            TYPE_QUICK_REPLY,
            pojo.message.timeStampUnixNano,
            convertToQuickReplyList(pojoAttribute),
            pojo.source
        )
    }

    private fun convertToQuickReplyList(quickReplyListPojo: QuickReplyAttachmentAttributes?): List<QuickReplyUiModel> {
        val list = ArrayList<QuickReplyUiModel>()
        if (quickReplyListPojo != null && !quickReplyListPojo.quickReplies.isEmpty()) {
            for (pojo in quickReplyListPojo.quickReplies) {
                if (!TextUtils.isEmpty(pojo.text)) {
                    list.add(QuickReplyUiModel(pojo.text, pojo.value, pojo.action))
                }
            }
        }
        return list
    }

    private fun convertToReplyBubble(pojo: ChatSocketPojo, jsonAttributes: JsonObject): MessageUiModel {
        val pojoAttribute = GsonBuilder().create().fromJson(
            jsonAttributes,
            ChatSocketPojo::class.java
        )

        return MessageUiModel.Builder()
            .withResponseFromWs(pojo)
            .withParentReply(pojoAttribute.parentReply)
            .build()
    }
}
