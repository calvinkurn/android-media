package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by yfsx on 08/05/18.
 */

class FallbackAttachmentViewModel(msgId: String,
                                  fromUid: String,
                                  from: String,
                                  fromRole: String,
                                  attachmentId: String,
                                  attachmentType: String,
                                  replyTime: String,
                                  message: String) : BaseChatViewModel(msgId, fromUid, from,
        fromRole, attachmentId, attachmentType, replyTime, message)
        , Visitable<BaseChatTypeFactory> {

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
