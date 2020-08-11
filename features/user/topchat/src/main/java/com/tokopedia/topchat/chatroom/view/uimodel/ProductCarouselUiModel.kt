package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ProductCarouselUiModel constructor(
        val products: List<Visitable<*>>,
        messageId: String, fromUid: String?, from: String, fromRole: String,
        attachmentId: String, attachmentType: String, replyTime: String?, message: String,
        source: String
) : BaseChatViewModel(
        messageId, fromUid, from, fromRole,
        attachmentId, attachmentType, replyTime, message, source
), Visitable<TopChatTypeFactory> {
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}