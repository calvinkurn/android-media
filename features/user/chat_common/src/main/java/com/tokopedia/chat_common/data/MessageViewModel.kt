package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by nisie on 5/16/18.
 */
open class MessageViewModel : SendableViewModel, Visitable<BaseChatTypeFactory> {

    var blastId: Long = 0
    var fraudStatus = 0

    constructor(
            reply: Reply
    ) : super(
            messageId = reply.msgId.toString(),
            fromUid = reply.senderId.toString(),
            from = reply.senderName,
            fromRole = reply.role,
            attachmentId = reply.attachment.id,
            attachmentType = reply.attachment.type.toString(),
            replyTime = reply.replyTime,
            startTime = "",
            isRead = reply.isRead,
            isDummy = false,
            isSender = !reply.isOpposite,
            message = reply.msg,
            source = reply.source,
    ) {
        blastId = reply.blastId
        fraudStatus = reply.fraudStatus
    }

    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, startTime: String,
            isRead: Boolean, isDummy: Boolean, isSender: Boolean, message: String,
            source: String, blastId: Long = 0, fraudStatus: Int = 0
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, startTime,
            isRead, isDummy, isSender, message,
            source
    ) {
        this.blastId = blastId
        this.fraudStatus = fraudStatus
    }

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
    constructor(
            messageId: String, fromUid: String, from: String, fromRole: String,
            attachmentId: String, attachmentType: String, replyTime: String, startTime: String,
            message: String, isRead: Boolean, isDummy: Boolean, isSender: Boolean,
            source: String
    ) : super(
            messageId, fromUid, from, fromRole,
            attachmentId, attachmentType, replyTime, startTime,
            isRead, isDummy, isSender, message, source
    ) {
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
    constructor(
            messageId: String, fromUid: String, from: String, startTime: String,
            message: String
    ) : super(
            messageId, fromUid, from, "",
            "", "", BaseChatViewModel.SENDING_TEXT, startTime,
            false, true, true, message, ""
    ) {
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isFromAutoReply(): Boolean {
        return source == SOURCE_AUTO_REPLY
    }

    fun isFromSmartReply(): Boolean {
        return source == SOURCE_TOPBOT
    }

    fun isFromBroadCast(): Boolean {
        return blastId > 0
    }

    fun isBanned(): Boolean {
        return fraudStatus == 1
    }
}
