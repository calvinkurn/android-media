package com.tokopedia.tokochat.common.view.chatroom.uimodel

import com.tokopedia.tokochat.common.view.chatroom.uimodel.base.TokoChatSendableBaseUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatMessageBubbleUiModel protected constructor(
    builder: Builder
) : TokoChatSendableBaseUiModel(builder) {

    var messageText: String = builder.messageText
        private set

    var isNotSupported: Boolean = builder.isNotSupported
        private set

    var label: String = builder.label
        private set

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatMessageBubbleUiModel>() {
        internal var messageText: String = ""
        internal var isNotSupported: Boolean = false
        internal var label: String = ""

        fun withMessageText(messageText: String): Builder {
            this.messageText = messageText
            return self()
        }

        fun withIsNotSupported(isNotSupported: Boolean): Builder {
            this.isNotSupported = isNotSupported
            return self()
        }

        fun withLabel(label: String): Builder {
            this.label = label
            return self()
        }

        override fun build(): TokoChatMessageBubbleUiModel {
            return TokoChatMessageBubbleUiModel(this)
        }
    }
}
