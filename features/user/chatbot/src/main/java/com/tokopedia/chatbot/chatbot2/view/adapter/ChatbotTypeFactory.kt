package com.tokopedia.chatbot.chatbot2.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicAttachmentTextUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicOwocInvoiceUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment.DynamicStickyButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.rating.ChatRatingUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.stickyactionbutton.StickyActionButtonUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel

/**
 * @author by nisie on 27/11/18.
 */
interface ChatbotTypeFactory : AdapterTypeFactory {

    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    fun createViewHolder(
        parent: ViewGroup,
        type: Int,
        chatbotAdapterListener: ChatbotAdapterListener
    ): AbstractViewHolder<*>

    fun type(attachInvoiceSelectionUiModel: AttachInvoiceSelectionUiModel): Int

    fun type(quickReplyListUiModel: QuickReplyListUiModel): Int

    fun type(chatRating: ChatRatingUiModel): Int

    fun type(chatBubble: ChatActionSelectionBubbleUiModel): Int

    fun type(chatSepratorUiModel: ChatSepratorUiModel): Int

    fun type(helpFullQuestionsUiModel: HelpFullQuestionsUiModel): Int

    fun type(csatOptionsUiModel: CsatOptionsUiModel): Int

    fun type(stickyActionButtonUiModel: StickyActionButtonUiModel): Int

    fun type(videoUploadUiModel: VideoUploadUiModel): Int

    fun type(attachInvoiceSentUiModel: AttachInvoiceSentUiModel): Int

    fun type(
        attachInvoiceSentUiModel: com.tokopedia.chatbot.chatbot2.attachinvoice.data.uimodel.AttachInvoiceSentUiModel
    ): Int

    fun type(dynamicStickyButtonUiModel: DynamicStickyButtonUiModel): Int

    fun type(dynamicAttachmentTextUiModel: DynamicAttachmentTextUiModel): Int

    fun type(dynamicOwocInvoiceUiModel: DynamicOwocInvoiceUiModel): Int
}
