package com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.ChatbotConstant.RENDER_TO_UI_BASED_ON_STATUS
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotTypeFactory
import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel

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
    var status: Int = RENDER_TO_UI_BASED_ON_STATUS,
    var isTypingBlocked: Boolean = false
) :
    BaseChatUiModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source),
    Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
