package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationPojo
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class QuotationUiModel constructor(
        private val quotationPojo: QuotationPojo,
        messageId: String, fromUid: String, from: String, fromRole: String,
        attachmentId: String, attachmentType: String, replyTime: String, isSender: Boolean,
        message: String, startTime: String = "", isRead: Boolean = false, isDummy: Boolean = false,
        source: String
) : SendableViewModel(
        messageId, fromUid, from, fromRole,
        attachmentId, attachmentType, replyTime, startTime,
        isRead, isDummy, isSender, message,
        source
), Visitable<TopChatTypeFactory> {

    val quotationId get() = quotationPojo.identifier
    val price get() = quotationPojo.price
    val title get() = quotationPojo.title
    val thumbnailUrl get() = quotationPojo.thumbnail
    val url get() = quotationPojo.url

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}