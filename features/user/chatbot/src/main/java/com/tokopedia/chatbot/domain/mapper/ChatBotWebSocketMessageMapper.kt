package com.tokopedia.chatbot.domain.mapper

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_RATING
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICES_SELECTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY_SEND
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonPojo
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonViewModel
import com.tokopedia.chatbot.domain.pojo.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.domain.pojo.invoicelist.websocket.InvoicesSelectionPojo
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */
const val TYPE_HELPFULL_QUESTION = "22"
const val TYPE_CSAT_OPTIONS = "23"
const val TYPE_STICKED_BUTTON_ACTIONS = "25"
class ChatBotWebSocketMessageMapper @Inject constructor() : WebsocketMessageMapper() {

    override fun map(pojo: ChatSocketPojo): Visitable<*> {
        return if (pojo.showRating || pojo.ratingStatus != 0) {
            convertToChatRating(pojo)
        } else
            super.map(pojo)
    }

    override fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: JsonObject): Visitable<*> {
        return when (pojo.attachment?.type) {
            TYPE_QUICK_REPLY -> convertToQuickReplyModel(pojo, jsonAttributes)
            TYPE_INVOICES_SELECTION -> convertToInvoiceSelection(pojo, jsonAttributes)
            TYPE_CHAT_BALLOON_ACTION -> convertToChatActionSelectionBubbleModel(pojo, jsonAttributes)
            TYPE_QUICK_REPLY_SEND -> convertToMessageViewModel(pojo)
            TYPE_HELPFULL_QUESTION -> convertToHelpQuestionViewModel(pojo)
            TYPE_CSAT_OPTIONS -> convertToCsatOptionsViewModel(pojo)
            TYPE_STICKED_BUTTON_ACTIONS -> convertToStickedButtonActionsViewModel(pojo)
            else -> super.mapAttachmentMessage(pojo, jsonAttributes)
        }
    }

    private fun convertToStickedButtonActionsViewModel(pojo: ChatSocketPojo): Visitable<*> {
        val stickyActionButtonPojo = GsonBuilder().create()
                .fromJson<StickyActionButtonPojo>(pojo.attachment?.attributes,
                        StickyActionButtonPojo::class.java)
        return StickyActionButtonViewModel(
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
                .fromJson<HelpFullQuestionPojo>(pojo.attachment?.attributes,
                        HelpFullQuestionPojo::class.java)
        return HelpFullQuestionsViewModel(
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
                .fromJson<CsatAttributesPojo>(pojo.attachment?.attributes,
                        CsatAttributesPojo::class.java)
        return CsatOptionsViewModel(
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
                .fromJson<QuickReplyAttachmentAttributes>(pojo.attachment?.attributes,
                        QuickReplyAttachmentAttributes::class.java)
        return ChatRatingViewModel(
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


    private fun convertToInvoiceSelection(pojo: ChatSocketPojo,
                                          jsonAttribute: JsonObject): AttachInvoiceSelectionViewModel {
        val invoiceListKey = "invoice_list"
        val jsonObject = jsonAttribute.getAsJsonObject(invoiceListKey)

        val invoicesSelectionPojo = GsonBuilder().create().fromJson<InvoicesSelectionPojo>(jsonObject, InvoicesSelectionPojo::class.java)
        val invoiceList = invoicesSelectionPojo.invoices

        val list = ArrayList<AttachInvoiceSingleViewModel>()

        for (invoice in invoiceList) {
            val attributes = invoice.attributes
            val attachInvoice = AttachInvoiceSingleViewModel(
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
                    attributes.amount)
            list.add(attachInvoice)
        }

        return AttachInvoiceSelectionViewModel(
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

    private fun convertToChatActionSelectionBubbleModel(pojo: ChatSocketPojo, jsonAttribute: JsonObject): ChatActionSelectionBubbleViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ChatActionBalloonSelectionAttachmentAttributes>(jsonAttribute, ChatActionBalloonSelectionAttachmentAttributes::class.java)
        val quickReplyPojo = GsonBuilder().create()
                .fromJson<QuickReplyAttachmentAttributes>(jsonAttribute,
                        QuickReplyAttachmentAttributes::class.java)
        return ChatActionSelectionBubbleViewModel(
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
            pojo: ChatActionBalloonSelectionAttachmentAttributes): List<ChatActionBubbleViewModel> {
        val result = ArrayList<ChatActionBubbleViewModel>()
        for (item in pojo.chatActions) {
            result.add(ChatActionBubbleViewModel(item.text, item.value, item.action, hexColor = item.hexColor, iconUrl = item.iconUrl))
        }
        return result
    }

    private fun convertToQuickReplyModel(pojo: ChatSocketPojo, jsonAttribute: JsonObject): QuickReplyListViewModel {
        val pojoAttribute = GsonBuilder().create()
                .fromJson<QuickReplyAttachmentAttributes>(jsonAttribute,
                        QuickReplyAttachmentAttributes::class.java)
        return QuickReplyListViewModel(
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

    private fun convertToQuickReplyList(quickReplyListPojo: QuickReplyAttachmentAttributes?): List<QuickReplyViewModel> {
        val list = ArrayList<QuickReplyViewModel>()
        if (quickReplyListPojo != null && !quickReplyListPojo.quickReplies.isEmpty()) {
            for (pojo in quickReplyListPojo.quickReplies) {
                if (!TextUtils.isEmpty(pojo.text)) {
                    list.add(QuickReplyViewModel(pojo.text, pojo.value, pojo.action))
                }
            }
        }
        return list
    }
}