package com.tokopedia.chatbot.data.invoice

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.ChatbotConstant.RENDER_INVOICE_LIST_AND_BUTTON_ACTION
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * Created by Hendri on 27/03/18.
 */

class AttachInvoiceSelectionUiModel(
    messageId: String,
    fromUid: String,
    from: String,
    fromRole: String,
    attachmentId: String,
    attachmentType: String,
    replyTime: String,
    var list: List<AttachInvoiceSingleUiModel>?,
    message: String,
    source: String,
    var status: Int = RENDER_INVOICE_LIST_AND_BUTTON_ACTION
) : BaseChatUiModel(
    messageId, fromUid,
    from, fromRole, attachmentId, attachmentType, replyTime, message, source
),
    Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
