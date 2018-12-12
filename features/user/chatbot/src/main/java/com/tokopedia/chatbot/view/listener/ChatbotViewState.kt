package com.tokopedia.chatbot.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatbotViewState : BaseChatViewState {
    fun onSuccessLoadFirstTime(list: ArrayList<Visitable<*>>)

    fun onCheckToHideQuickReply(visitable: Visitable<*>)

    fun onReceiveQuickReplyEvent(visitable: QuickReplyListViewModel)

    fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel)
}