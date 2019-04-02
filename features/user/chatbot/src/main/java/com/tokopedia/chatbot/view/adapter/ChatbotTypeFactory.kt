package com.tokopedia.chatbot.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel

/**
 * @author by nisie on 27/11/18.
 */
interface ChatbotTypeFactory : AdapterTypeFactory{

    fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int

    fun type(attachInvoiceSelectionViewModel: AttachInvoiceSelectionViewModel): Int

    fun type(quickReplyListViewModel: QuickReplyListViewModel): Int

    fun type(chatRating: ChatRatingViewModel): Int

    fun type(chatBubble: ChatActionSelectionBubbleViewModel): Int

}