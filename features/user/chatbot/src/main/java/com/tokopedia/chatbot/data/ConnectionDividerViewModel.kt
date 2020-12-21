package com.tokopedia.chatbot.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

class ConnectionDividerViewModel(messageId: String = "",
                                 fromUid: String = "",
                                 from: String = "",
                                 fromRole: String = "",
                                 attachmentId: String = "",
                                 attachmentType: String = "",
                                 replyTime: String = "",
                                 message: String = "",
                                 source: String = "",
                                 val dividerMessage: String?,
                                 val isShowButton: Boolean,
                                 val type: String,
                                 val leaveQueue: (() -> Unit)?) :
        BaseChatViewModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source), Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is ConnectionDividerViewModel
    }

}