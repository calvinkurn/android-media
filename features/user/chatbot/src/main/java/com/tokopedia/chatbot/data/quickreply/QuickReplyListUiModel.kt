package com.tokopedia.chatbot.data.quickreply

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
 * @author by yfsx on 08/05/18.
 */

class QuickReplyListUiModel constructor(
    msgId: String = "",
    fromUid: String = "",
    from: String = "",
    fromRole: String = "",
    message: String = "",
    attachmentId: String = "",
    attachmentType: String = "",
    replyTime: String = "",
    var quickReplies: List<QuickReplyUiModel> = ArrayList(),
    source: String = ""
) :
    BaseChatUiModel(
        msgId, fromUid, from, fromRole, attachmentId,
        attachmentType, replyTime, message, source
    ),
    Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
