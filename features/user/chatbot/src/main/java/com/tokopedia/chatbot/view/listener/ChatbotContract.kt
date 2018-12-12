package com.tokopedia.chatbot.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo

/**
 * @author by nisie on 07/12/18.
 */
interface ChatbotContract {
    interface View : BaseChatContract.View {
        fun developmentView()
        fun onSuccessLoadFirstTime(dummyList: ArrayList<Visitable<*>>)
    }

    interface Presenter : BaseChatContract.Presenter {

        fun sendInvoiceAttachment(messageId: String, invoiceLinkPojo: InvoiceLinkPojo, startTime: String)

        fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel, generateStartTime: String)
        fun generateInvoice(invoiceLinkPojo: InvoiceLinkPojo, senderId : String)
                : AttachInvoiceSentViewModel
    }
}