package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
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
    ) : super (
            msgId,
            fromUid,
            from,
            fromRole,
            attachmentId,
            attachmentType,
            replyTime,
            "",
            true,
            false,
            !isOpposite,
            message,
            source
    )

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
