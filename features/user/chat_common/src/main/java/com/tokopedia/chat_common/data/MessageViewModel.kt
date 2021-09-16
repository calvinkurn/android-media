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
            return self()
        }

        override fun build(): MessageViewModel {
            return MessageViewModel(this)
        }
    }
}
