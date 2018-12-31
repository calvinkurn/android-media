package com.tokopedia.chatbot.view.listener

import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.view.listener.BaseChatContract
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo

/**
 * @author by nisie on 07/12/18.
 */
interface ChatbotContract {
    interface View : BaseChatContract.View {

    }

    interface Presenter : BaseChatContract.Presenter<View> {

        fun sendInvoiceAttachment(messageId: String, invoiceLinkPojo: InvoiceLinkPojo, startTime: String)

        fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel, startTime: String)

        fun generateInvoice(invoiceLinkPojo: InvoiceLinkPojo, senderId: String)
                : AttachInvoiceSentViewModel

        fun getExistingChat(messageId: String,
                            onError: (Throwable) -> Unit,
                            onSuccess: (ChatroomViewModel) -> Unit)

        fun connectWebSocket(messageId: String)

        fun sendRating(rating: Int, onError: (Throwable) -> Unit,
                       onSuccess: () -> Unit)

        fun sendReasonRating()

        fun sendActionBubble()

        fun destroyWebSocket()


    }
}