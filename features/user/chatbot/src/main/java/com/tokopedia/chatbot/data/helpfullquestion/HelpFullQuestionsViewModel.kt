package com.tokopedia.chatbot.data.helpfullquestion

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

class HelpFullQuestionsViewModel(messageId: String = "",
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
) : BaseChatViewModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source)
        , Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
