package com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chatbot.ChatbotConstant
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotTypeFactory
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel

class DynamicStickyButtonUiModel(
    messageId: String = "",
    fromUid: String = "",
    from: String = "",
    fromRole: String = "",
    attachmentId: String = "",
    attachmentType: String = "",
    replyTime: String = "",
    message: String = "",
    source: String = "",
    var status: Int = ChatbotConstant.RENDER_TO_UI_BASED_ON_STATUS,
    val actionBubble: ChatActionBubbleUiModel,
    val contentText: String,
    var isShowButtonAction: Boolean = true
) : BaseChatUiModel(
    messageId, fromUid,
    from, fromRole, attachmentId, attachmentType, replyTime, message, source
),
    Visitable<ChatbotTypeFactory> {
    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
