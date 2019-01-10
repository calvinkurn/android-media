package com.tokopedia.topchat.revamp.view

import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.listener.BaseChatViewState

interface TopChatViewState : BaseChatViewState {

    abstract fun showErrorWebSocket(b: Boolean)

    abstract fun showRetryUploadImages(it: ImageUploadViewModel, b: Boolean)

    fun onSetCustomMessage(customMessage: String)

//    fun onSuccessLoadFirstTime(chatroomViewModel: ChatroomViewModel)
//
//    fun onCheckToHideQuickReply(visitable: Visitable<*>)
//
//    fun onReceiveQuickReplyEvent(visitable: QuickReplyListViewModel)

//    fun onShowInvoiceToChat(generatedInvoice: AttachInvoiceSentViewModel)

}
