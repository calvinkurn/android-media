package com.tokopedia.chatbot.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chat_common.data.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.ConnectionDividerViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsViewModel
import com.tokopedia.chatbot.data.helpfullquestion.HelpFullQuestionsViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel
import com.tokopedia.chatbot.data.seprator.ChatSepratorViewModel
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonViewModel

/**
 * @author by nisie on 27/11/18.
 */
interface ChatbotTypeFactory : AdapterTypeFactory {

    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    fun createViewHolder(
            parent: ViewGroup,
            type: Int,
            chatbotAdapterListener: ChatbotAdapterListener): AbstractViewHolder<*>

    fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int

    fun type(attachInvoiceSelectionViewModel: AttachInvoiceSelectionViewModel): Int

    fun type(quickReplyListViewModel: QuickReplyListViewModel): Int

    fun type(chatRating: ChatRatingViewModel): Int

    fun type(chatBubble: ChatActionSelectionBubbleViewModel): Int

    fun type(connectionDividerViewModel: ConnectionDividerViewModel): Int

    fun type(chatSepratorViewModel: ChatSepratorViewModel): Int

    fun type(helpFullQuestionsViewModel: HelpFullQuestionsViewModel): Int

    fun type(csatOptionsViewModel: CsatOptionsViewModel): Int

    fun type(stickyActionButtonViewModel: StickyActionButtonViewModel): Int

}