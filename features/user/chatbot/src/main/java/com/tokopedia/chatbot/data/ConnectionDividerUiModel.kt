package com.tokopedia.chatbot.data

import android.annotation.SuppressLint
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

/**
We don't need to implement hashCode or equals method as the comparison is only done for the object
not it's content
 */
@SuppressLint("EqualsOrHashCode")
class ConnectionDividerUiModel(
    messageId: String = "",
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
    val leaveQueue: (() -> Unit)?
) :
    BaseChatUiModel(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source), Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is ConnectionDividerUiModel
    }
}
