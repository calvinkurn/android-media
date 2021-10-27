package com.tokopedia.chatbot.data.chatactionbubble

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionSelectionBubbleViewModel constructor(messageId: String = "",
                                         fromUid: String = "",
                                         from: String = "",
                                         fromRole: String = "",
                                         attachmentId: String = "",
                                         attachmentType: String = "",
                                         replyTime: String = "",
                                         message: String = "",
                                         var chatActionList: List<ChatActionBubbleViewModel> = ArrayList(),
                                         var quickReplies: List<QuickReplyViewModel> = ArrayList(),
                                                     source: String = "")
    : BaseChatUiModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source)
        , Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
