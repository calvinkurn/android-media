package com.tokopedia.chat_common.data

import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.utils.time.TimeHelper
import java.util.*

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
 * @param attachmentType attachment type.
 * @param replyTime replytime in unixtime
 *
 * @see AttachmentType for attachment types.
 */

open class BaseChatViewModel constructor(
    val messageId: String,
    var fromUid: String?,
    val from: String,
    var fromRole: String,
    val attachmentId: String,
    val attachmentType: String,
    var replyTime: String?,
    var message: String,
    var source: String,
    val replyId: String = "",
    val localId: String = "",
    val blastId: Long = 0,
    val fraudStatus: Int = 0,
    val label: String = ""
) {

    constructor(builder: Builder<*, *>) : this(
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
    )

    /**
     * Set in [BaseChatAdapter]
     *
     * @param showDate set true to show date in header of chat
     */
    var isShowDate = false

    /**
     * Set in [BaseChatAdapter]
     *
     * @param showTime set true to show time in chat
     */
    var isShowTime = true

    companion object {
        const val SENDING_TEXT = "Sedang mengirim ..."
        const val SOURCE_AUTO_REPLY = "auto_reply"
        const val SOURCE_WELCOME_MESSAGE = "welcome_message"
        const val SOURCE_TOPBOT = "topbot"
        const val SOURCE_SMART_REPLY = "smart_reply"
        const val SOURCE_BLAST_SELLER = "blast_seller"
        const val SOURCE_REPLIED_BLAST = "replied_blast"
    }

    /**
     * Base builder for chat UI model
     * B: the Builder class that extend this builder class
     * UI: the UiModel class that extend BaseChatViewModel
     */
    abstract class Builder<
            out B : Builder<B, UI>,
            out UI : BaseChatViewModel
            > {

        var messageId: String = ""
            private set
        var fromUid: String = ""
            private set
        var from: String = ""
            private set
        var fromRole: String = ""
            private set
        var attachmentId: String = DEFAULT_ATTACHMENT_ID
            private set
        var attachmentType: String = DEFAULT_ATTACHMENT_TYPE
            private set
        var replyTime: String = ""
            private set
        var message: String = ""
            private set
        var source: String = ""
            private set
        var replyId: String = ""
            private set
        var localId: String = ""
            private set
        var blastId: Long = 0
            private set
        var fraudStatus: Int = 0
            private set
        var label: String = ""
            private set

        open fun withResponseFromGQL(
            reply: Reply
        ): B {
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
            withBlastId(reply.blastId)
            withFraudStatus(reply.fraudStatus)
            withLabel(reply.label)
            return self()
        }

        open fun withResponseFromWs(
            reply: ChatSocketPojo
        ): B {
            withMsgId(reply.msgId.toString())
            withFromUid(reply.fromUid)
            withFrom(reply.from)
            withFromRole(reply.fromRole)
            withAttachmentId(reply.attachment?.id ?: DEFAULT_ATTACHMENT_ID)
            withAttachmentType(reply.attachment?.type ?: DEFAULT_ATTACHMENT_TYPE)
            withReplyTime(reply.message.timeStampUnixNano)
            withMsg(reply.message.censoredReply)
            withSource(reply.source)
            withLabel(reply.label)
            withLocalId(reply.localId)
            return self()
        }

        open fun withResponseFromAPI(
            reply: ChatItemPojo
        ): B {
            withMsgId(reply.msgId.toString())
            withFromUid(reply.senderId)
            withFrom(reply.senderName)
            withFromRole(reply.role)
            withAttachmentId(reply.attachmentId.toString())
            withAttachmentType(reply.attachment?.type.toString())
            withReplyTime(System.currentTimeMillis().toString())
            withMsg(reply.msg)
            withSource(reply.source.orEmpty())
            return self()
        }

        open fun withRoomMetaData(
            roomMetaData: RoomMetaData
        ): B {
            withLocalId(IdentifierUtil.generateLocalId())
            withMsgId(roomMetaData.msgId)
            withFromUid(roomMetaData.sender.uid)
            withFrom(roomMetaData.sender.name)
            withFromRole(roomMetaData.sender.name)
            withReplyTime(generateCurrentReplyTime())
            return self()
        }

        fun withMsgId(messageId: String): B {
            this.messageId = messageId
            return self()
        }

        fun withFromUid(fromUid: String): B {
            this.fromUid = fromUid
            return self()
        }

        fun withFrom(from: String): B {
            this.from = from
            return self()
        }

        fun withFromRole(fromRole: String): B {
            this.fromRole = fromRole
            return self()
        }

        fun withAttachmentId(attachmentId: String): B {
            this.attachmentId = attachmentId
            return self()
        }

        fun withAttachmentType(attachmentType: String): B {
            this.attachmentType = attachmentType
            return self()
        }

        fun withReplyTime(replyTime: String): B {
            this.replyTime = replyTime
            return self()
        }

        fun withMsg(message: String): B {
            this.message = message
            return self()
        }

        fun withSource(source: String): B {
            this.source = source
            return self()
        }

        fun withReplyId(replyId: String): B {
            this.replyId = replyId
            return self()
        }

        fun withLocalId(localId: String): B {
            this.localId = localId
            return self()
        }

        fun withBlastId(blastId: Long): B {
            this.blastId = blastId
            return self()
        }

        fun withFraudStatus(fraudStatus: Int): B {
            this.fraudStatus = fraudStatus
            return self()
        }

        fun withLabel(label: String): B {
            this.label = label
            return self()
        }

        @Suppress("UNCHECKED_CAST")
        protected fun self(): B {
            return this as B
        }

        abstract fun build(): UI

        companion object {
            const val DEFAULT_ATTACHMENT_ID = ""
            const val DEFAULT_ATTACHMENT_TYPE = ""

            /**
             * replyTime needs to be on nano second format
             */
            fun generateCurrentReplyTime(): String {
                val currentTime = Calendar.getInstance()
                return (currentTime.timeInMillis * 1_000_000).toString()
            }
        }
    }
}
