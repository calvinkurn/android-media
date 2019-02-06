package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by nisie on 5/16/18.
 */
class MessageViewModel : SendableViewModel, Visitable<BaseChatTypeFactory> {
    constructor(messageId: String, fromUid: String, from: String, fromRole: String, attachmentId: String, attachmentType: String, replyTime: String, startTime: String, isRead: Boolean, isDummy: Boolean, isSender: Boolean, message: String) : super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, startTime, isRead, isDummy, isSender, message) {}

    /**
     * Constructor for WebSocketResponse / API Response
     * [ChatWebSocketListenerImpl]
     * [GetReplyListUseCase]
     *
     * @param messageId      messageId
     * @param fromUid        userId of sender
     * @param from           name of sender
     * @param fromRole       role of sender
     * @param attachmentId   attachment id
     * @param attachmentType attachment type. Please refer to
     * [WebSocketMapper] types
     * @param replyTime      replytime in unixtime
     * @param startTime      date time when sending / uploading data. Used to validate temporary
     * @param message        censored reply
     * @param isRead         is message already read by opponent
     * @param isSender       is own sender
     */
    constructor(messageId: String, fromUid: String, from: String, fromRole: String,
                attachmentId: String, attachmentType: String,
                replyTime: String, startTime: String, message: String, isRead: Boolean, isDummy: Boolean,
                isSender: Boolean)
            : super(messageId, fromUid, from, fromRole, attachmentId, attachmentType, replyTime, startTime, isRead, isDummy, isSender, message) {
    }

    /**
     * Constructor for send message
     *
     * @param messageId messageId
     * @param fromUid   userId of sender
     * @param from      name of sender
     * @param startTime date time when sending / uploading data. Used to validate temporary
     * message
     * @param message   censored reply
     */
    constructor(messageId: String, fromUid: String, from: String, startTime: String,
                message: String) : super(messageId, fromUid, from, "", "", "",
            BaseChatViewModel.SENDING_TEXT, startTime,
            false, true, true, message) {
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}
