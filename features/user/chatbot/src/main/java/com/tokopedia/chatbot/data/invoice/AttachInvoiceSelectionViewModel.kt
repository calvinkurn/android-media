package com.tokopedia.chatbot.data.invoice


import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * Created by Hendri on 27/03/18.
 */

class AttachInvoiceSelectionViewModel (messageId: String, fromUid: String, from: String,
                                      fromRole: String, attachmentId: String,
                                      attachmentType: String, replyTime: String,
                                      var list: List<AttachInvoiceSingleViewModel>?,
                                      message: String, source: String) : BaseChatUiModel(messageId, fromUid,
        from, fromRole, attachmentId, attachmentType, replyTime, message, source)
        , Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
