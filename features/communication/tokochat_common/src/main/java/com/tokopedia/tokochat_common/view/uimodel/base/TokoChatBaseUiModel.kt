package com.tokopedia.tokochat_common.view.uimodel.base

import com.tokopedia.tokochat_common.util.TokoChatValueUtil.STATUS_DELETED
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.STATUS_NORMAL
import java.util.Calendar

/**
 * TODO: Use general variable names for this class
 */
open class TokoChatBaseUiModel constructor(
    val messageId: String,
    var fromUserId: String,
    var fromRole: String,
    var replyTime: String,
    var message: String,
    val fraudStatus: Int = 0,
    val label: String = "",
    val bubbleStatus: Int = STATUS_NORMAL
) {

    constructor(builder: Builder<*, *>) : this(
        messageId = builder.messageId,
        fromUserId = builder.fromUid,
        fromRole = builder.fromRole,
        replyTime = builder.replyTime,
        message = builder.message,
        fraudStatus = builder.fraudStatus,
        label = builder.label,
        bubbleStatus = builder.bubbleStatus
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

    fun isDeleted(): Boolean {
        return bubbleStatus == STATUS_DELETED
    }

    fun isNormal(): Boolean {
        return bubbleStatus == STATUS_NORMAL
    }

    /**
     * Base builder for chat UI model
     * B: the Builder class that extend this builder class
     * UI: the UiModel class that extend TokoChatBaseUiModel
     */
    abstract class Builder<
        out B : Builder<B, UI>,
        out UI : TokoChatBaseUiModel
        > {

        internal var messageId: String = ""
        internal var fromUid: String = ""
        internal var fromRole: String = ""
        internal var replyTime: String = ""
        internal var message: String = ""
        internal var fraudStatus: Int = 0
        internal var label: String = ""
        internal var bubbleStatus: Int = STATUS_NORMAL

        fun withMarkAsDeleted(): B {
            withBubbleStatus(STATUS_DELETED)
            return self()
        }

        fun withMsgId(messageId: String): B {
            this.messageId = messageId
            return self()
        }

        fun withBubbleStatus(bubbleStatus: Int): B {
            this.bubbleStatus = bubbleStatus
            return self()
        }

        fun withFromUid(fromUid: String): B {
            this.fromUid = fromUid
            return self()
        }

        fun withFromRole(fromRole: String): B {
            this.fromRole = fromRole
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
            private const val TIME_MULTIPLIER = 1_000_000

            /**
             * replyTime needs to be on nano second format
             */
            fun generateCurrentReplyTime(): String {
                val currentTime = Calendar.getInstance()
                return (currentTime.timeInMillis * TIME_MULTIPLIER).toString()
            }
        }
    }
}
