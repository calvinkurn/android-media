package com.tokopedia.chatbot.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.chatbot.data.AttachInvoiceSelectionViewModel
import com.tokopedia.chatbot.data.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel

/**
 * @author by nisie on 27/11/18.
 */
interface ChatbotTypeFactory {

    fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*>

    fun type(attachInvoiceSentViewModel: AttachInvoiceSentViewModel): Int

    fun type(attachInvoiceSelectionViewModel: AttachInvoiceSelectionViewModel): Int

    fun type(quickReplyListViewModel: QuickReplyListViewModel): Int

}