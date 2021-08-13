package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by yfsx on 08/05/18.
 */

class FallbackAttachmentViewModel : MessageViewModel,
        Visitable<BaseChatTypeFactory> {

    constructor(
            msgId: String,
            fromUid: String,
            from: String,
            fromRole: String,
            attachmentId: String,
            attachmentType: String,
            replyTime: String,
            message: String,
            isOpposite: Boolean,
            source: String
    ) : super(
            messageId = msgId,
            fromUid = fromUid,
            from = from,
            fromRole = fromRole,
            attachmentId = attachmentId,
            attachmentType = attachmentType,
            replyTime = replyTime,
            startTime = "",
            isRead = true,
            isDummy = false,
            isSender = !isOpposite,
            message = message,
            source = source
    )

    constructor(reply: Reply) : super(
            messageId = reply.msgId.toString(),
            fromUid = reply.senderId.toString(),
            from = reply.senderName,
            fromRole = reply.role,
            attachmentId = reply.attachment.id,
            attachmentType = reply.attachment.type.toString(),
            replyTime = reply.replyTime,
            startTime = reply.replyTime,
            isRead = reply.isRead,
            isDummy = false,
            message = reply.attachment.fallback.html,
            isSender = !reply.isOpposite,
            source = reply.source
    )

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
