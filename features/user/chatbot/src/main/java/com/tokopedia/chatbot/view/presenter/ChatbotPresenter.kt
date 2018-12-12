package com.tokopedia.chatbot.view.presenter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.domain.GetChatUseCase
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.presenter.BaseChatPresenter
import com.tokopedia.chatbot.data.invoice.AttachInvoiceSentViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.domain.mapper.ChatbotWebsocketMessageMapper
import com.tokopedia.chatbot.domain.pojo.InvoiceLinkPojo
import com.tokopedia.chatbot.view.listener.ChatbotContract
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 05/12/18.
 */
class ChatbotPresenter @Inject constructor(
        override var getChatUseCase: GetChatUseCase,
        override var userSession: UserSessionInterface,
        var chatbotWebsocketMessageMapper: ChatbotWebsocketMessageMapper)
    : BaseChatPresenter(userSession, getChatUseCase, chatbotWebsocketMessageMapper), ChatbotContract.Presenter {

    override fun sendInvoiceAttachment(messageId: String,
                                       invoiceLinkPojo: InvoiceLinkPojo,
                                       startTime: String) {
        // webSocketUseCase.execute(webSocketUseCase.getParamSendInvoiceAttachment(messageId,
//        invoice, startTime));
    }

    override fun sendQuickReply(messageId: String, quickReply: QuickReplyViewModel,
                                generateStartTime: String) {


    }

    override fun generateInvoice(invoiceLinkPojo: InvoiceLinkPojo, senderId: String):
            AttachInvoiceSentViewModel {
        val invoiceLinkAttributePojo = invoiceLinkPojo.attributes
        return AttachInvoiceSentViewModel(
                senderId,
                userSession.name,
                invoiceLinkAttributePojo.title,
                invoiceLinkAttributePojo.description,
                invoiceLinkAttributePojo.imageUrl,
                invoiceLinkAttributePojo.totalAmount,
                SendableViewModel.generateStartTime()
        )
    }

    override fun mapToVisitable(pojo: ChatSocketPojo): Visitable<*> {
        return chatbotWebsocketMessageMapper.map(pojo)
    }
}