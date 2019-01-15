package com.tokopedia.chatbot.domain.mapper

import android.text.TextUtils
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_RATING
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICES_SELECTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY_SEND
import com.tokopedia.chat_common.domain.mapper.WebsocketMessageMapper
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.domain.pojo.InvoiceSentPojo
import com.tokopedia.chatbot.domain.pojo.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.invoicelist.websocket.InvoicesSelectionPojo
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import java.util.*
import javax.inject.Inject

/**
 * @author by nisie on 10/12/18.
 */
class ChatBotWebSocketMessageMapper @Inject constructor() : WebsocketMessageMapper() {

    override fun map(pojo: ChatSocketPojo): Visitable<*> {
        return if (pojo.showRating || pojo.ratingStatus != 0) {
            convertToChatRating(pojo)
        } else
            super.map(pojo)
    }

    override fun mapAttachmentMessage(pojo: ChatSocketPojo, jsonAttributes: String): Visitable<*> {
        return when (pojo.attachment?.type.toString()) {
            TYPE_QUICK_REPLY -> convertToQuickReplyModel(pojo, jsonAttributes)
            TYPE_INVOICE_SEND -> convertToInvoiceSent(pojo, jsonAttributes)
            TYPE_INVOICES_SELECTION -> convertToInvoiceSelection(pojo, jsonAttributes)
            TYPE_CHAT_BALLOON_ACTION -> convertToChatActionSelectionBubbleModel(pojo, jsonAttributes)
            TYPE_QUICK_REPLY_SEND -> convertToMessageViewModel(pojo)
            else -> super.mapAttachmentMessage(pojo, jsonAttributes)
        }
    }

    private fun convertToChatRating(pojo: ChatSocketPojo): Visitable<*> {
        return ChatRatingViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.message.censoredReply,
                "",
                TYPE_CHAT_RATING,
                pojo.message.timeStampUnixNano,
                pojo.ratingStatus,
                pojo.message.timeStampUnixNano.toLong()
        )
    }


    private fun convertToInvoiceSelection(pojo: ChatSocketPojo,
                                          jsonAttribute: String): AttachInvoiceSelectionViewModel {
        val invoiceListKey = "invoice_list"
        val parser = JsonParser()
        val o: JsonObject = parser.parse(jsonAttribute).asJsonObject
        val jsonObject = o.getAsJsonObject(invoiceListKey)

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
                pojo.attachment?.id.toString(),
                pojo.attachment?.type.toString(),
                pojo.message.timeStampUnixNano,
                list,
                pojo.message.censoredReply
        )

    }

    private fun convertToInvoiceSent(pojo: ChatSocketPojo, jsonAttribute: String):
            AttachInvoiceSentViewModel {
        val invoiceSentPojo = GsonBuilder().create().fromJson<InvoiceSentPojo>(jsonAttribute,
                InvoiceSentPojo::class.java)
        return AttachInvoiceSentViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.attachment?.id.toString(),
                pojo.attachment?.type.toString(),
                pojo.message.timeStampUnixNano,
                pojo.startTime,
                invoiceSentPojo.invoiceLink.attributes.title,
                invoiceSentPojo.invoiceLink.attributes.description,
                invoiceSentPojo.invoiceLink.attributes.imageUrl,
                invoiceSentPojo.invoiceLink.attributes.totalAmount,
                !pojo.isOpposite
        )

    }


    private fun convertToChatActionSelectionBubbleModel(pojo: ChatSocketPojo, jsonAttribute: String): ChatActionSelectionBubbleViewModel {
        val pojoAttribute = GsonBuilder().create().fromJson<ChatActionBalloonSelectionAttachmentAttributes>(jsonAttribute, ChatActionBalloonSelectionAttachmentAttributes::class.java)
        return ChatActionSelectionBubbleViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.attachment?.id.toString(),
                pojo.attachment?.type.toString(),
                pojo.message.timeStampUnixNano,
                pojo.message.censoredReply,
                convertToChatActionBubbleViewModelList(pojoAttribute)
        )
    }

    private fun convertToChatActionBubbleViewModelList(
            pojo: ChatActionBalloonSelectionAttachmentAttributes): List<ChatActionBubbleViewModel> {
        val result = ArrayList<ChatActionBubbleViewModel>()
        for (item in pojo.chatActions) {
            result.add(ChatActionBubbleViewModel(item.text, item.value, item.action))
        }
        return result
    }

    private fun convertToQuickReplyModel(pojo: ChatSocketPojo, jsonAttribute: String): QuickReplyListViewModel {
        val pojoAttribute = GsonBuilder().create()
                .fromJson<QuickReplyAttachmentAttributes>(jsonAttribute,
                        QuickReplyAttachmentAttributes::class.java)
        return QuickReplyListViewModel(
                pojo.msgId.toString(),
                pojo.fromUid,
                pojo.from,
                pojo.fromRole,
                pojo.message.censoredReply,
                pojo.attachment?.id.toString(),
                TYPE_QUICK_REPLY,
                pojo.message.timeStampUnixNano,
                convertToQuickReplyList(pojoAttribute)
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