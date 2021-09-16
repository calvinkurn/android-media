package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

open class MessageViewModel
/**
 * Primary constructor, use [Builder] class to create this instance.
 */
protected constructor(builder: Builder) : SendableViewModel(
    messageId = builder.messageId,
    fromUid = builder.fromUid,
    from = builder.from,
    fromRole = builder.fromRole,
    attachmentId = builder.attachmentId,
    attachmentType = builder.attachmentType,
    replyTime = builder.replyTime,
    message = builder.message,
    source = builder.source,
    replyId = builder.replyId,
    localId = builder.localId,
    blastId = builder.blastId,
    fraudStatus = builder.fraudStatus,
    label = builder.label,
    startTime = builder.startTime,
    isRead = builder.isRead,
    isDummy = builder.isDummy,
    isSender = builder.isSender,
), Visitable<BaseChatTypeFactory> {

    var attachment: Any? = null
        private set

    init {
        this.attachment = builder.attachment
    }

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isFromAutoReply(): Boolean {
        return source == SOURCE_AUTO_REPLY
    }

    fun isFromSmartReply(): Boolean {
        return source == SOURCE_TOPBOT || source.startsWith(SOURCE_SMART_REPLY)
    }

    fun isFromBroadCast(): Boolean {
        return blastId > 0
    }

    fun isBanned(): Boolean {
        return fraudStatus == 1
    }

    fun hasLabel(): Boolean {
        return label.isNotEmpty()
    }

    fun hasAttachment(): Boolean {
        return attachment != null
    }

    open class Builder : SendableViewModel.Builder<Builder, MessageViewModel>() {

        var attachment: Any? = null
            private set

        fun withAttachment(attachment: Any): Builder {
            this.attachment = attachment
            return this
        }

        fun withResponseFromGQL(
            reply: Reply
        ): Builder {
            withMsgId(reply.msgId.toString())
            withFromUid(reply.senderId.toString())
            withFrom(reply.senderName)
            withFromRole(reply.role)
            withAttachmentId(reply.attachment.id)
            withAttachmentType(reply.attachment.type.toString())
            withReplyTime(reply.replyTime)
            withMsg(reply.msg)
            withSource(reply.source)
            withReplyId(reply.replyId)
            withIsRead(reply.isRead)
            withIsSender(!reply.isOpposite)
            withBlastId(reply.blastId)
            withFraudStatus(reply.fraudStatus)
            withLabel(reply.label)
            withIsDummy(false)
            return self()
        }

        fun withResponseFromWs(
            reply: ChatSocketPojo
        ): Builder {
            withMsgId(reply.msgId.toString())
            withFromUid(reply.fromUid)
            withFrom(reply.from)
            withFromRole(reply.fromRole)
            withAttachmentId(reply.attachment?.id ?: DEFAULT_ATTACHMENT_ID)
            withAttachmentType(reply.attachment?.type ?: DEFAULT_ATTACHMENT_TYPE)
            withReplyTime(reply.message.timeStampUnixNano)
            withStartTime(reply.startTime)
            withMsg(reply.message.censoredReply)
            withSource(reply.source)
            withIsSender(!reply.isOpposite)
            withLabel(reply.label)
            withLocalId(reply.localId)
            return self()
        }

        fun withResponseFromAPI(
            reply: ChatItemPojo
        ): Builder {
            withMsgId(reply.msgId.toString())
            withFromUid(reply.senderId)
            withFrom(reply.senderName)
            withFromRole(reply.role)
            withAttachmentId(reply.attachmentId.toString())
            withAttachmentType(reply.attachment?.type.toString())
            withReplyTime(System.currentTimeMillis().toString())
            withMsg(reply.msg)
            withSource(reply.source.orEmpty())
            withIsSender(true)
            return self()
        }

        override fun build(): MessageViewModel {
            return MessageViewModel(this)
        }
    }
}
