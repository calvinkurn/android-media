package com.tokopedia.tokochat_common.view.uimodel

import com.tokopedia.tokochat_common.view.uimodel.base.SendableUiModel

/**
 * Primary constructor, use [Builder] class to create this instance.
 */
open class MessageBubbleUiModel protected constructor(
    builder: Builder
) : SendableUiModel(builder) {

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

    open class Builder : SendableUiModel.Builder<Builder, MessageBubbleUiModel>() {

        internal var attachment: Any? = null

        fun withAttachment(attachment: Any): Builder {
            this.attachment = attachment
            return self()
        }

        override fun build(): MessageBubbleUiModel {
            return MessageBubbleUiModel(this)
        }
    }
}
