package com.tokopedia.chatbot.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.AttachInvoiceSentUiModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleUiModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsUiModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListUiModel
import com.tokopedia.chatbot.data.rating.ChatRatingUiModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorUiModel
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonUiModel
import com.tokopedia.chatbot.data.videoupload.VideoUploadUiModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener

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

    fun type(videoUploadUiModel: VideoUploadUiModel) : Int

    fun type(attachInvoiceSentUiModel: AttachInvoiceSentUiModel): Int

    fun type(attachInvoiceSentUiModel: com.tokopedia.chatbot.attachinvoice.data.uimodel.AttachInvoiceSentUiModel): Int
}
