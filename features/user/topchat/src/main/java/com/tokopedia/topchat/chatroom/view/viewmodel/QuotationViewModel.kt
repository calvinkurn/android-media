package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.QuotationPojo
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class QuotationViewModel(
        messageId: String,
        fromUid: String,
        from: String,
        fromRole: String,
        attachmentId: String,
        attachmentType: String,
        replyTime: String,
        isRead: Boolean,
        isSender: Boolean,
        message: String,
        private val quotationPojo: QuotationPojo
) : SendableViewModel(
        messageId,
        fromUid,
        from,
        fromRole,
        attachmentId,
        attachmentType,
        replyTime,
        "",
        isRead,
        false,
        isSender,
        message
), Visitable<TopChatTypeFactory> {

    val price get() = quotationPojo.price
    val title get() = quotationPojo.title
    val thumbnailUrl get() = quotationPojo.thumbnail

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}