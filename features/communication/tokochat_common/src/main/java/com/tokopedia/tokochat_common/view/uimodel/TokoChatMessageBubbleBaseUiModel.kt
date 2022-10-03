package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatMessageBubbleBaseUiModel protected constructor(
    builder: Builder
) : TokoChatSendableBaseUiModel(builder) {

    var messageText: String = builder.messageText
        private set

    /**
     * Any attachment type : Image, Sticker, Voice Note, etc
     */
    var attachment: Any? = builder.attachment
        private set

    var isNotSupported: Boolean = builder.isNotSupported
        private set

    var label: String = builder.label
        private set

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatMessageBubbleBaseUiModel>() {
        internal var messageText: String = ""
        internal var attachment: Any? = null
        internal var isNotSupported: Boolean = false
        internal var label: String = ""

        fun withMessageText(messageText: String): Builder {
            this.messageText = messageText
            return self()
        }

        fun withAttachment(attachment: Any): Builder {
            this.attachment = attachment
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

        override fun build(): TokoChatMessageBubbleBaseUiModel {
            return TokoChatMessageBubbleBaseUiModel(this)
        }
    }
}
