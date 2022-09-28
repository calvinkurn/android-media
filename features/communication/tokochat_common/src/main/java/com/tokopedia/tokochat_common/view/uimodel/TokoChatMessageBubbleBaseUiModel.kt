package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.tokochat_common.view.uimodel.base.TokoChatSendableBaseUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 */
open class TokoChatMessageBubbleBaseUiModel protected constructor(
    builder: Builder
) : TokoChatSendableBaseUiModel(builder) {

    var attachment: Any? = builder.attachment
        private set

    fun isBanned(): Boolean {
        return fraudStatus == 1
    }

    fun hasLabel(): Boolean {
        return label.isNotEmpty()
    }

    fun hasAttachment(): Boolean {
        return attachment != null
    }

    open class Builder : TokoChatSendableBaseUiModel.Builder<Builder, TokoChatMessageBubbleBaseUiModel>() {

        internal var attachment: Any? = null

        fun withAttachment(attachment: Any): Builder {
            this.attachment = attachment
            return self()
        }

        override fun build(): TokoChatMessageBubbleBaseUiModel {
            return TokoChatMessageBubbleBaseUiModel(this)
        }
    }
}
