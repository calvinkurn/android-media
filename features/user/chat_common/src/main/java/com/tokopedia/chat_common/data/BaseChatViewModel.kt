package com.tokopedia.chat_common.data

import com.tokopedia.chat_common.data.parentreply.ParentReply
import com.tokopedia.chat_common.domain.pojo.ChatItemPojo
import com.tokopedia.chat_common.domain.pojo.ChatSocketPojo
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import java.util.*

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
    val label: String = "",
    val parentReply: ParentReply? = null
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
        parentReply = builder.parentReply
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

    fun getReferredImageUrl(): String {
        return ""
    }

    fun hasReplyBubble(): Boolean {
        return parentReply != null
    }

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

        internal var messageId: String = ""
        internal var fromUid: String = ""
        internal var from: String = ""
        internal var fromRole: String = ""
        internal var attachmentId: String = DEFAULT_ATTACHMENT_ID
        internal var attachmentType: String = DEFAULT_ATTACHMENT_TYPE
        internal var replyTime: String = ""
        internal var message: String = ""
        internal var source: String = ""
        internal var replyId: String = ""
        internal var localId: String = ""
        internal var blastId: Long = 0
        internal var fraudStatus: Int = 0
        internal var label: String = ""
        internal var parentReply: ParentReply? = null

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
            withParentReply(reply.parentReply)
            withOrGenerateLocalId("")
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
            withOrGenerateLocalId(reply.localId)
            withParentReply(reply.parentReply)
            withFraudStatus(reply.fraudStatus)
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

        fun withOrGenerateLocalId(localId: String): B {
            val finalLocalId = if (localId.isEmpty()) {
                IdentifierUtil.generateLocalId()
            } else localId
            return withLocalId(finalLocalId)
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

        fun withParentReply(parentReply: ParentReply?): B {
            this.parentReply = parentReply
            return self()
        }

        fun withReferredMsg(referredMsg: BaseChatViewModel?): B {
            if (referredMsg == null) return self()
            val parentReply = ParentReply(
                attachmentId = referredMsg.attachmentId,
                attachmentType = referredMsg.attachmentType,
                senderId = referredMsg.fromUid ?: "",
                replyTime = referredMsg.replyTime ?: "",
                mainText = referredMsg.message,
                subText = "",
                imageUrl = referredMsg.getReferredImageUrl(),
                localId = referredMsg.localId,
                source = "chat"
            )
            return withParentReply(parentReply)
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
