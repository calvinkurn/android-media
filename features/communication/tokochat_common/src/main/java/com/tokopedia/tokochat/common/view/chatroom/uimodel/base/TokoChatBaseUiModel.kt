package com.tokopedia.tokochat.common.view.chatroom.uimodel.base

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatBaseUiModel constructor(
    var messageId: String,
    var fromUserId: String,
    var messageTime: Long
) {

    constructor(builder: Builder<*, *>) : this(
        messageId = builder.messageId,
        fromUserId = builder.fromUserId,
        messageTime = builder.messageTime
    )

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
        internal var fromUserId: String = ""
        internal var messageTime: Long = 0

        fun withMessageId(messageId: String): B {
            this.messageId = messageId
            return self()
        }

        fun withFromUserId(fromUserId: String): B {
            this.fromUserId = fromUserId
            return self()
        }

        fun withMessageTime(messageTime: Long): B {
            this.messageTime = messageTime
            return self()
        }

        @Suppress("UNCHECKED_CAST")
        protected fun self(): B {
            return this as B
        }

        abstract fun build(): UI
    }
}
