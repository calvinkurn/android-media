package com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.chatbot2.data.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotTypeFactory

class HelpFullQuestionsUiModel(
    messageId: String = "",
    fromUid: String = "",
    from: String = "",
    fromRole: String = "",
    attachmentId: String = "",
    attachmentType: String = "",
    replyTime: String = "",
    message: String = "",
    var helpfulQuestion: HelpFullQuestionPojo.HelpfulQuestion?,
    source: String = "",
    var isSubmited: Boolean = false
) : BaseChatUiModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source),
    Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
