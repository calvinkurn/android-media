package com.tokopedia.chatbot.view.presenter

import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.InvoiceLinkPojo
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 05/12/18.
 */
class ChatbotPresenter @Inject constructor(
        override var getChatUseCase: GetChatUseCase,
        override var userSession: UserSessionInterface)
    : BaseChatPresenter(userSession, getChatUseCase), ChatbotContract.Presenter {

    override fun sendInvoiceAttachment(messageId: String,
                                       invoiceLinkPojo: InvoiceLinkPojo,
                                       startTime: String) {
        // webSocketUseCase.execute(webSocketUseCase.getParamSendInvoiceAttachment(messageId,
//        invoice, startTime));
    }

    override fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel,
                                generateStartTime: String) {


    }


}