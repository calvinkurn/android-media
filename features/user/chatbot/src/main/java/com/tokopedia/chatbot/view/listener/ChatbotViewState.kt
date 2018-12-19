package com.tokopedia.chatbot.view.listener

import android.app.Activity
import android.support.v4.app.FragmentActivity
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.ChatroomViewModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.rating.ChatRatingViewModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatbotViewState : BaseChatViewState {
    fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel)

    fun onCheckToHideQuickReply(visitable: Visitable<*>)

    fun onReceiveQuickReplyEvent(visitable: QuickReplyListViewModel)

    fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel)

    fun onSendRating(element: ChatRatingViewModel, rating: Int)

    fun onSuccessSendRating(element: ChatRatingViewModel, rating: Int, activity: Activity,
                            onClickReasonRating: Unit)

    fun onSendingMessage(messageId: String, userId: String, name: String, sendMessage: String)

}