package com.tokopedia.chatbot.data.chatactionbubble

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.ChatbotConstant.RENDER_INVOICE_LIST_AND_BUTTON_ACTION
import com.tokopedia.chatbot.data.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionSelectionBubbleUiModel constructor(
    messageId: String = "",
    fromUid: String = "",
    from: String = "",
    fromRole: String = "",
    attachmentId: String = "",
    attachmentType: String = "",
    replyTime: String = "",
    message: String = "",
    var chatActionList: List<ChatActionBubbleUiModel> = ArrayList(),
    var quickReplies: List<QuickReplyUiModel> = ArrayList(),
    source: String = "",
    var status: Int = RENDER_INVOICE_LIST_AND_BUTTON_ACTION
) :
    BaseChatUiModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source),
    Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
