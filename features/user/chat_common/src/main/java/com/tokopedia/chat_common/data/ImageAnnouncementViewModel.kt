package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by nisie on 5/15/18.
 */
class ImageAnnouncementViewModel
/**
 * Constructor for WebSocketResponse / API Response
 *
 * @param messageId      messageId
 * @param fromUid        userId of sender
 * @param from           name of sender
 * @param fromRole       role of sender
 * @param attachmentId   attachment id
 * @param attachmentType attachment type.
 * @param replyTime      replytime in unixtime
 * @param imageUrl       image url
 * @param redirectUrl    redirect url in http
 * @param blastId        blast id for campaign
 * @see AttachmentType for attachment types.
 */
(messageId: String, fromUid: String, from: String,
 fromRole: String, attachmentId: String, attachmentType: String,
 replyTime: String, val imageUrl: String, val redirectUrl: String,
 message: String, val blastId: Int) : BaseChatViewModel(messageId,
        fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message)
        , Visitable<BaseChatTypeFactory> {

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
