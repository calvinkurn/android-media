package com.tokopedia.chatbot.data.chatactionbubble

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionSelectionBubbleViewModel(messageId: String, fromUid: String, from: String,
                                         fromRole: String, attachmentId: String,
                                         attachmentType: String, replyTime: String,
                                         message: String, var chatActionList: List<ChatActionBubbleViewModel>)
    : BaseChatViewModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message)
        , Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
