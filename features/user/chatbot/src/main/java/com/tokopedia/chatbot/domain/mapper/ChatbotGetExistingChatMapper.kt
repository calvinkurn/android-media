package com.tokopedia.chatbot.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICES_SELECTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.pojo.InvoiceSentPojo
import com.tokopedia.chatbot.domain.pojo.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.ListInvoicesSelectionPojo
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyPojo
import javax.inject.Inject

/**
 * @author by nisie on 21/12/18.
 */
open class ChatbotGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    override fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment?.type.toString()) {
            TYPE_QUICK_REPLY -> convertToQuickReply(chatItemPojoByDateByTime)
            TYPE_CHAT_BALLOON_ACTION -> convertToBalloonAction(chatItemPojoByDateByTime)
            TYPE_INVOICES_SELECTION -> convertToInvoicesSelection(chatItemPojoByDateByTime)
            TYPE_INVOICE_SEND -> convertToInvoiceSent(chatItemPojoByDateByTime)
            else -> super.mapAttachment(chatItemPojoByDateByTime)
        }
    }

    /////////////////// QUICK REPLIES

    private fun convertToQuickReply(reply: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create()
                .fromJson<QuickReplyAttachmentAttributes>(reply.attachment?.attributes,
                        QuickReplyAttachmentAttributes::class.java)
        return QuickReplyListViewModel(
                reply.msgId.toString(),
                reply.senderId.toString(),
                reply.senderName,
                reply.role,
                reply.msg,
                reply.attachment?.id.toString(),
                reply.attachment?.type.toString(),
                reply.replyTime,
                convertToQuickRepliesList(pojoAttribute.quickReplies)


        )
    }

    private fun convertToQuickRepliesList(quickReplies: List<QuickReplyPojo>): List<QuickReplyViewModel> {
        val list: ArrayList<QuickReplyViewModel> = ArrayList()
        for (pojo in quickReplies) {
            list.add(QuickReplyViewModel(pojo.text,
                    pojo.value,
                    pojo.action))
        }
        return list
    }

    /////////////////// CHAT BALLOON


    private fun convertToBalloonAction(pojo: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create().fromJson<ChatActionBalloonSelectionAttachmentAttributes>(
                pojo.attachment?.attributes, ChatActionBalloonSelectionAttachmentAttributes::class.java)
        return ChatActionSelectionBubbleViewModel(
                pojo.msgId.toString(),
                pojo.senderId.toString(),
                pojo.senderName,
                pojo.role,
                pojo.attachment?.id.toString(),
                pojo.attachment?.type.toString(),
                pojo.replyTime,
                pojo.msg,
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

    /////////////////// INVOICE SELECTION

    private fun convertToInvoicesSelection(pojo: Reply): AttachInvoiceSelectionViewModel {
        val invoicesSelectionPojo = GsonBuilder().create().fromJson<ListInvoicesSelectionPojo>(pojo
                .attachment?.attributes, ListInvoicesSelectionPojo::class.java)
        val invoiceAttr = invoicesSelectionPojo.invoices
        val invoiceList = invoiceAttr.invoices

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
                pojo.senderId.toString(),
                pojo.senderName,
                pojo.role,
                pojo.attachment?.id.toString(),
                pojo.attachment?.type.toString(),
                pojo.replyTime,
                list,
                pojo.msg
        )
    }

    /////////////////// INVOICE SEND


    private fun convertToInvoiceSent(pojo: Reply): AttachInvoiceSentViewModel {
        val invoiceSentPojo = GsonBuilder().create().fromJson<InvoiceSentPojo>(
                pojo.attachment?.attributes, InvoiceSentPojo::class.java)
        return AttachInvoiceSentViewModel(
                pojo.msgId.toString(),
                pojo.senderId.toString(),
                pojo.senderName,
                pojo.role,
                pojo.attachment?.id.toString(),
                pojo.attachment?.type.toString(),
                pojo.replyTime,
                invoiceSentPojo.invoiceLink.attributes.title,
                invoiceSentPojo.invoiceLink.attributes.description,
                invoiceSentPojo.invoiceLink.attributes.imageUrl,
                invoiceSentPojo.invoiceLink.attributes.totalAmount,
                !pojo.isOpposite,
                pojo.isRead
        )

    }

}
