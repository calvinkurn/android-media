package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

/**
 * Created by Hendri on 22/06/18.
 */
class ImageDualAnnouncementUiModel
/**
 * Constructor for WebSocketResponse / API Response
 *
 * @param messageId         messageId
 * @param fromUid           userId of sender
 * @param from              name of sender
 * @param fromRole          role of sender
 * @param attachmentId      attachment id
 * @param attachmentType    attachment type. Please refer to
 * @param replyTime         replytime in unixtime
 * @param imageUrlTop       image url Top image
 * @param redirectUrlTop    redirect url in http for Top image click
 * @param imageUrlBottom    image url Bottom image
 * @param redirectUrlBottom redirect url in http for Bottom image click
 * @param blastId           blast id for campaign.
 */
constructor(
        messageId: String,
        fromUid: String,
        from: String,
        fromRole: String,
        attachmentId: String,
        attachmentType: String,
        replyTime: String,
        message: String,
        var imageUrlTop: String,
        var redirectUrlTop: String,
        var imageUrlBottom: String,
        var redirectUrlBottom: String,
        val blastId: Long,
        source: String
) : BaseChatViewModel(
        messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, message, source
), Visitable<TopChatTypeFactory> {

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
