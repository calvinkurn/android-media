package com.tokopedia.chatbot.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICES_SELECTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY_SEND
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSingleViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonPojo
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonViewModel
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.SHOW_TEXT
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_AGENT_QUEUE
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_CHAT_SEPRATOR
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_OPTION_LIST
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_CSAT_VIEW
import com.tokopedia.chatbot.domain.mapper.ChatbotGetExistingChatMapper.Companion.TYPE_STICKY_BUTTON
import com.tokopedia.chatbot.domain.pojo.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.chatdivider.ChatDividerResponse
import com.tokopedia.chatbot.domain.pojo.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.domain.pojo.quickreply.ListInvoicesSelectionPojo
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.domain.pojo.quickreply.QuickReplyPojo
import javax.inject.Inject

/**
 * @author by nisie on 21/12/18.
 */
open class ChatbotGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    object Companion {
        const val  SHOW_TEXT = "show"
        const val TYPE_CHAT_SEPRATOR = "15"
        const val TYPE_AGENT_QUEUE = "16"
        const val TYPE_OPTION_LIST = "22"
        const val TYPE_CSAT_OPTIONS = "23"
        const val TYPE_STICKY_BUTTON = "25"
        const val TYPE_CSAT_VIEW = "13"
    }

    override fun mapAttachment(chatItemPojoByDateByTime: Reply): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment?.type.toString()) {
            TYPE_QUICK_REPLY -> convertToQuickReply(chatItemPojoByDateByTime)
            TYPE_QUICK_REPLY_SEND -> convertToMessageViewModel(chatItemPojoByDateByTime)
            TYPE_CHAT_BALLOON_ACTION -> convertToBalloonAction(chatItemPojoByDateByTime)
            TYPE_INVOICES_SELECTION -> convertToInvoicesSelection(chatItemPojoByDateByTime)
            TYPE_CHAT_SEPRATOR->convertToChatSeprator(chatItemPojoByDateByTime)
            TYPE_AGENT_QUEUE->convertToChatDivider(chatItemPojoByDateByTime)
            TYPE_OPTION_LIST -> convertToHelpQuestionViewModel(chatItemPojoByDateByTime)
            TYPE_CSAT_OPTIONS-> convertToCsatOptionsViewModel(chatItemPojoByDateByTime)
            TYPE_STICKY_BUTTON-> convertToStickyButtonActionsViewModel(chatItemPojoByDateByTime)
            TYPE_CSAT_VIEW-> convertToMessageViewModel(chatItemPojoByDateByTime)
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
                reply.attachment?.id ?: "",
                reply.attachment?.type.toString(),
                reply.replyTime,
                convertToQuickRepliesList(pojoAttribute.quickReplies),
                reply.source
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
                pojo.attachment?.id ?: "",
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
                pojo.attachment?.id ?: "",
                pojo.attachment?.type.toString(),
                pojo.replyTime,
                list,
                pojo.msg,
                pojo.source
        )
    }


    /////////// CHAT DIVIDER
    
    private fun convertToChatDivider(pojo: Reply):ConnectionDividerViewModel{
        val chatDividerPojo = GsonBuilder().create().fromJson<ChatDividerResponse>(pojo
                .attachment?.attributes, ChatDividerResponse::class.java)
        return ConnectionDividerViewModel(
                replyTime = pojo.replyTime,
                dividerMessage = chatDividerPojo?.divider?.label,
                isShowButton = false,
                type = SHOW_TEXT,
                leaveQueue = null
        )
    }

    /////////// CHAT SEPRATOR

    private fun convertToChatSeprator(pojo: Reply):ChatSepratorViewModel{
        val chatDividerPojo = GsonBuilder().create().fromJson<ChatDividerResponse>(pojo
                .attachment?.attributes, ChatDividerResponse::class.java)
        return ChatSepratorViewModel(
                replyTime = pojo.replyTime,
                sepratorMessage = chatDividerPojo?.divider?.label,
                dividerTiemstamp = pojo.replyTime)
    }

    /////////// HELPFULL QUESTIONS

    private fun convertToHelpQuestionViewModel(chatItemPojoByDateByTime: Reply): HelpFullQuestionsViewModel {
        val helpFullQuestionPojo = GsonBuilder().create()
                .fromJson<HelpFullQuestionPojo>(chatItemPojoByDateByTime.attachment?.attributes,
                        HelpFullQuestionPojo::class.java)
        return HelpFullQuestionsViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id ?: "",
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                chatItemPojoByDateByTime.msg,
                helpFullQuestionPojo.helpfulQuestion,
                chatItemPojoByDateByTime.source
        )
    }

    /////////// CSAT OPTIONS

    private fun convertToCsatOptionsViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val csatAttributesPojo = GsonBuilder().create()
                .fromJson<CsatAttributesPojo>(chatItemPojoByDateByTime.attachment?.attributes,
                        CsatAttributesPojo::class.java)
        return CsatOptionsViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment?.id ?: "",
                chatItemPojoByDateByTime.attachment?.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                chatItemPojoByDateByTime.msg,
                csatAttributesPojo.csat,
                chatItemPojoByDateByTime.source
        )
    }

    /////////// STICKY BUTTON

    private fun convertToStickyButtonActionsViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val stickyActionButtonPojo = GsonBuilder().create()
                .fromJson<StickyActionButtonPojo>(chatItemPojoByDateByTime.attachment?.attributes,
                        StickyActionButtonPojo::class.java)
        return StickyActionButtonViewModel(
                chatItemPojoByDateByTime.msgId.toString(),
                chatItemPojoByDateByTime.senderId.toString(),
                chatItemPojoByDateByTime.senderName,
                chatItemPojoByDateByTime.role,
                chatItemPojoByDateByTime.attachment.id,
                chatItemPojoByDateByTime.attachment.type.toString(),
                chatItemPojoByDateByTime.replyTime,
                chatItemPojoByDateByTime.msg,
                stickyActionButtonPojo.stickedButtonActions,
                chatItemPojoByDateByTime.source
        )
    }

}
