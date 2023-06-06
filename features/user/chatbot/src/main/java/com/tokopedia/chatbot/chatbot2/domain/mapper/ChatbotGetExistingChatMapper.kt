package com.tokopedia.chatbot.chatbot2.domain.mapper

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_CHAT_BALLOON_ACTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICES_SELECTION
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_INVOICE_SEND
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY
import com.tokopedia.chat_common.data.AttachmentType.Companion.TYPE_QUICK_REPLY_SEND
import com.tokopedia.chat_common.data.FallbackAttachmentUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.domain.mapper.GetExistingChatMapper
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CHAT_SEPARATOR
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CSAT_OPTIONS
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_CSAT_VIEW
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_HELPFULL_QUESTION
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_SECURE_IMAGE_UPLOAD
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_STICKY_BUTTON
import com.tokopedia.chatbot.ChatbotConstant.AttachmentType.TYPE_VIDEO_UPLOAD
import com.tokopedia.chatbot.ChatbotConstant.DynamicAttachment.DYNAMIC_ATTACHMENT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.pojo.InvoiceSentPojo
import com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionPojo
import com.tokopedia.chatbot.chatbot2.data.csatoptionlist.CsatAttributesPojo
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.DynamicAttachment
import com.tokopedia.chatbot.chatbot2.data.dynamicAttachment.DynamicStickyButton
import com.tokopedia.chatbot.chatbot2.data.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.chatbot2.data.imageupload.ChatbotImageUploadAttributes
import com.tokopedia.chatbot.chatbot2.data.quickreply.ListInvoicesSelectionPojo
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyAttachmentAttributes
import com.tokopedia.chatbot.chatbot2.data.quickreply.QuickReplyPojo
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasonsSend
import com.tokopedia.chatbot.chatbot2.data.stickyactionbutton.StickyActionButtonPojo
import com.tokopedia.chatbot.chatbot2.data.uploadsecure.ChatbotVideoUploadAttributes
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicAttachmentTextUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSingleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.stickyactionbutton.StickyActionButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel
import javax.inject.Inject

/**
 * @author by nisie on 21/12/18.
 */
open class ChatbotGetExistingChatMapper @Inject constructor() : GetExistingChatMapper() {

    override fun mapAttachment(
        chatItemPojoByDateByTime: Reply,
        attachmentIds: List<String>
    ): Visitable<*> {
        return when (chatItemPojoByDateByTime.attachment.type.toString()) {
            TYPE_QUICK_REPLY -> convertToQuickReply(chatItemPojoByDateByTime)
            TYPE_QUICK_REPLY_SEND -> convertToMessageViewModel(chatItemPojoByDateByTime)
            TYPE_CHAT_BALLOON_ACTION -> convertToBalloonAction(chatItemPojoByDateByTime)
            TYPE_INVOICES_SELECTION -> convertToInvoicesSelection(chatItemPojoByDateByTime)
            TYPE_CHAT_SEPARATOR -> convertToChatSeprator(chatItemPojoByDateByTime)
            TYPE_HELPFULL_QUESTION -> convertToHelpQuestionViewModel(chatItemPojoByDateByTime)
            TYPE_CSAT_OPTIONS -> convertToCsatOptionsViewModel(chatItemPojoByDateByTime)
            TYPE_STICKY_BUTTON -> convertToStickyButtonActionsViewModel(
                chatItemPojoByDateByTime
            )
            TYPE_CSAT_VIEW -> convertToMessageViewModel(chatItemPojoByDateByTime)
            TYPE_SECURE_IMAGE_UPLOAD -> convertToImageUpload(chatItemPojoByDateByTime)
            TYPE_INVOICE_SEND -> convertToInvoiceSentUiModel(
                chatItemPojoByDateByTime,
                attachmentIds
            )
            TYPE_VIDEO_UPLOAD -> convertToVideoUpload(chatItemPojoByDateByTime)
            DYNAMIC_ATTACHMENT -> {
                val dynamicAttachment = gson.fromJson(
                    chatItemPojoByDateByTime.attachment.attributes,
                    DynamicAttachment::class.java
                )
                val contentCode =
                    dynamicAttachment.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes?.contentCode
                if (ChatbotConstant.DynamicAttachment.PROCESS_TO_VISITABLE_DYNAMIC_ATTACHMENT.contains(
                        contentCode
                    )
                ) {
                    when (contentCode) {
                        ChatbotConstant.DynamicAttachment.DYNAMIC_STICKY_BUTTON_RECEIVE -> convertToDynamicAttachmentwithContentCode105(
                            chatItemPojoByDateByTime,
                            dynamicAttachment
                        )
                        ChatbotConstant.DynamicAttachment.DYNAMIC_TEXT_SEND -> convertToDynamicAttachment105withContentCode106(
                            chatItemPojoByDateByTime,
                            dynamicAttachment
                        )
                        ChatbotConstant.DynamicAttachment.DYNAMIC_REJECT_REASON -> convertToDynamicAttachmentWithContentCode107(
                            chatItemPojoByDateByTime,
                            dynamicAttachment
                        )
                        ChatbotConstant.DynamicAttachment.DYNAMIC_REJECT_REASON_SEND -> convertToDynamicAttachmentWithContentCode108(
                            chatItemPojoByDateByTime,
                            dynamicAttachment
                        )
                        else -> convertToDynamicAttachmentFallback(chatItemPojoByDateByTime)
                    }
                } else {
                    convertToDynamicAttachmentFallback(chatItemPojoByDateByTime)
                }
            }
            else -> super.mapAttachment(chatItemPojoByDateByTime, attachmentIds)
        }
    }

    private fun convertToDynamicAttachmentwithContentCode105(
        pojo: Reply,
        dynamicAttachment: DynamicAttachment
    ): DynamicStickyButtonUiModel {
        val dynamicStickyButton = gson.fromJson(
            dynamicAttachment.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes?.dynamicContent,
            DynamicStickyButton::class.java
        )
        return DynamicStickyButtonUiModel(
            messageId = pojo.msgId,
            fromUid = pojo.senderId,
            from = pojo.senderName,
            fromRole = pojo.role,
            attachmentId = pojo.attachment.id,
            attachmentType = pojo.attachmentType.toString(),
            actionBubble = convertToSingleButtonAction(dynamicStickyButton.buttonAction),
            contentText = dynamicStickyButton.textMessage,
            replyTime = pojo.replyTime,
            status = pojo.status
        )
    }

    private fun convertToDynamicAttachment105withContentCode106(
        pojo: Reply,
        dynamicAttachment: DynamicAttachment
    ): DynamicAttachmentTextUiModel {
        val dynamicStickyButton = gson.fromJson(
            dynamicAttachment.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes?.dynamicContent,
            ChatActionPojo::class.java
        )
        return DynamicAttachmentTextUiModel.Builder()
            .withResponseFromGQL(pojo)
            .withMsgContent(dynamicStickyButton.text)
            .build()
    }

    private fun convertToDynamicAttachmentWithContentCode107(
        pojo: Reply,
        dynamicAttachment: DynamicAttachment
    ): DynamicAttachmentTextUiModel {
        val dynamicStickyButton = gson.fromJson(
            dynamicAttachment.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes?.dynamicContent,
            DynamicAttachmentRejectReasons::class.java
        )

        return DynamicAttachmentTextUiModel.Builder()
            .withResponseFromGQL(pojo)
            .isSender(false)
            .withMsg(dynamicStickyButton.helpfulQuestion.message)
            .build()
    }

    private fun convertToDynamicAttachmentWithContentCode108(
        pojo: Reply,
        dynamicAttachment: DynamicAttachment
    ): DynamicAttachmentTextUiModel {
        val dynamicStickyButton = gson.fromJson(
            dynamicAttachment.dynamicAttachmentAttribute?.dynamicAttachmentBodyAttributes?.dynamicContent,
            DynamicAttachmentRejectReasonsSend::class.java
        )
        var msg = ""
        if (dynamicStickyButton.helpfulQuestionFeedbackForm.helpfulQuestion.newQuickReplies.isNotEmpty()) {
            msg = dynamicStickyButton.helpfulQuestionFeedbackForm.helpfulQuestion.newQuickReplies[0].text
        }
        return DynamicAttachmentTextUiModel.Builder()
            .withResponseFromGQL(pojo)
            .withMsgContent(msg)
            .build()
    }

    private fun convertToSingleButtonAction(pojo: ChatActionPojo): ChatActionBubbleUiModel {
        return ChatActionBubbleUiModel(
            pojo.text,
            pojo.value,
            pojo.action
        )
    }

    /**
     * We are using Dynamic Attachment with content_codes like 100,101 [As of now]. In future more will
     * get added. If the user doesn't have the updated version to receive new content_code, we will
     * show message to update the app
     * */
    private fun convertToDynamicAttachmentFallback(chatItemPojoByDateByTime: Reply): Visitable<*> {
        var fallbackMessage = ""
        chatItemPojoByDateByTime.attachment.fallback.let {
            fallbackMessage = it.message
        }
        return FallbackAttachmentUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .withMsg(fallbackMessage)
            .withAttachment(chatItemPojoByDateByTime.attachment)
            .build()
    }

    private fun convertToInvoiceSentUiModel(pojo: Reply, attachmentIds: List<String>): com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel {
        val invoiceAttributes = pojo.attachment.attributes
        val invoiceSentPojo = gson.fromJson(invoiceAttributes, InvoiceSentPojo::class.java)
        val needSync = attachmentIds.contains(pojo.attachment.id)
        return com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel.Builder()
            .withResponseFromGQL(pojo)
            .withNeedSync(needSync)
            .withInvoiceAttributesResponse(invoiceSentPojo.invoiceLink)
            .build()
    }

    // ///////////////// QUICK REPLIES

    private fun convertToQuickReply(reply: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create()
            .fromJson<QuickReplyAttachmentAttributes>(
                reply.attachment.attributes,
                QuickReplyAttachmentAttributes::class.java
            )
        return QuickReplyListUiModel(
            reply.msgId.toString(),
            reply.senderId.toString(),
            reply.senderName,
            reply.role,
            reply.msg,
            reply.attachment.id,
            reply.attachment.type.toString(),
            reply.replyTime,
            convertToQuickRepliesList(pojoAttribute.quickReplies),
            reply.source
        )
    }

    private fun convertToQuickRepliesList(quickReplies: List<QuickReplyPojo>): List<QuickReplyUiModel> {
        val list: ArrayList<QuickReplyUiModel> = ArrayList()
        for (pojo in quickReplies) {
            list.add(
                QuickReplyUiModel(
                    pojo.text,
                    pojo.value,
                    pojo.action
                )
            )
        }
        return list
    }

    // ///////////////// CHAT BALLOON

    private fun convertToBalloonAction(pojo: Reply): Visitable<*> {
        val pojoAttribute = GsonBuilder().create().fromJson(
            pojo.attachment.attributes,
            com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes::class.java
        )
        return ChatActionSelectionBubbleUiModel(
            pojo.msgId.toString(),
            pojo.senderId.toString(),
            pojo.senderName,
            pojo.role,
            pojo.attachment.id,
            pojo.attachment.type.toString(),
            pojo.replyTime,
            pojo.msg,
            convertToChatActionBubbleViewModelList(pojoAttribute),
            status = pojo.status
        )
    }

    private fun convertToChatActionBubbleViewModelList(
        pojo: com.tokopedia.chatbot.chatbot2.data.chatactionballoon.ChatActionBalloonSelectionAttachmentAttributes
    ): List<ChatActionBubbleUiModel> {
        val result = ArrayList<ChatActionBubbleUiModel>()
        for (item in pojo.chatActions) {
            result.add(ChatActionBubbleUiModel(item.text, item.value, item.action))
        }
        return result
    }

    // ///////////////// INVOICE SELECTION

    private fun convertToInvoicesSelection(pojo: Reply): AttachInvoiceSelectionUiModel {
        val invoicesSelectionPojo = GsonBuilder().create().fromJson<ListInvoicesSelectionPojo>(
            pojo
                .attachment.attributes,
            ListInvoicesSelectionPojo::class.java
        )
        val invoiceAttr = invoicesSelectionPojo.invoices
        val invoiceList = invoiceAttr.invoices

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
            pojo.msgId,
            pojo.senderId,
            pojo.senderName,
            pojo.role,
            pojo.attachment.id,
            pojo.attachment.type.toString(),
            pojo.replyTime,
            list,
            pojo.msg,
            pojo.source,
            pojo.status
        )
    }

    // ///////// CHAT SEPRATOR

    private fun convertToChatSeprator(pojo: Reply): ChatSepratorUiModel {
        val chatDividerPojo = GsonBuilder().create().fromJson(
            pojo
                .attachment.attributes,
            com.tokopedia.chatbot.chatbot2.data.chatdivider.ChatDividerResponse::class.java
        )
        return ChatSepratorUiModel(
            replyTime = pojo.replyTime,
            sepratorMessage = chatDividerPojo?.divider?.label,
            dividerTiemstamp = pojo.replyTime
        )
    }

    // ///////// HELPFULL QUESTIONS

    private fun convertToHelpQuestionViewModel(chatItemPojoByDateByTime: Reply): HelpFullQuestionsUiModel {
        val helpFullQuestionPojo = GsonBuilder().create()
            .fromJson<HelpFullQuestionPojo>(
                chatItemPojoByDateByTime.attachment.attributes,
                HelpFullQuestionPojo::class.java
            )
        return HelpFullQuestionsUiModel(
            chatItemPojoByDateByTime.msgId.toString(),
            chatItemPojoByDateByTime.senderId.toString(),
            chatItemPojoByDateByTime.senderName,
            chatItemPojoByDateByTime.role,
            chatItemPojoByDateByTime.attachment.id,
            chatItemPojoByDateByTime.attachment.type.toString(),
            chatItemPojoByDateByTime.replyTime,
            chatItemPojoByDateByTime.msg,
            helpFullQuestionPojo.helpfulQuestion,
            chatItemPojoByDateByTime.source
        )
    }

    // ///////// CSAT OPTIONS

    private fun convertToCsatOptionsViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val csatAttributesPojo = GsonBuilder().create()
            .fromJson<CsatAttributesPojo>(
                chatItemPojoByDateByTime.attachment.attributes,
                CsatAttributesPojo::class.java
            )
        return CsatOptionsUiModel(
            chatItemPojoByDateByTime.msgId.toString(),
            chatItemPojoByDateByTime.senderId.toString(),
            chatItemPojoByDateByTime.senderName,
            chatItemPojoByDateByTime.role,
            chatItemPojoByDateByTime.attachment.id,
            chatItemPojoByDateByTime.attachment.type.toString(),
            chatItemPojoByDateByTime.replyTime,
            chatItemPojoByDateByTime.msg,
            csatAttributesPojo.csat,
            chatItemPojoByDateByTime.source
        )
    }

    // ///////// STICKY BUTTON

    private fun convertToStickyButtonActionsViewModel(chatItemPojoByDateByTime: Reply): Visitable<*> {
        val stickyActionButtonPojo = GsonBuilder().create()
            .fromJson(
                chatItemPojoByDateByTime.attachment.attributes,
                StickyActionButtonPojo::class.java
            )
        return StickyActionButtonUiModel(
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

    private fun convertToImageUpload(chatItemPojoByDateByTime: Reply):
        ImageUploadUiModel {
        val pojoAttribute = gson.fromJson(
            chatItemPojoByDateByTime.attachment.attributes,
            ChatbotImageUploadAttributes::class.java
        )

        return ImageUploadUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .withImageUrl(pojoAttribute.imageUrlSecure)
            .withImageUrlThumbnail(pojoAttribute.thumbnail)
            .build()
    }

    private fun convertToVideoUpload(chatItemPojoByDateByTime: Reply):
        VideoUploadUiModel {
        val pojoAttribute = gson.fromJson(
            chatItemPojoByDateByTime.attachment.attributes,
            ChatbotVideoUploadAttributes::class.java
        )

        return VideoUploadUiModel.Builder()
            .withResponseFromGQL(chatItemPojoByDateByTime)
            .withVideoUrl(pojoAttribute.videoUrl)
            .build()
    }
}
