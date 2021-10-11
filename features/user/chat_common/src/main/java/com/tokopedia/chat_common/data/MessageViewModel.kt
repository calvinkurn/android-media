package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.MessageViewModel.Builder
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * Primary constructor, use [Builder] class to create this instance.
 */
open class MessageViewModel protected constructor(
    builder: Builder
) : SendableViewModel(builder), Visitable<BaseChatTypeFactory> {

    var attachment: Any? = builder.attachment
        private set

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

        internal var attachment: Any? = null

        fun withAttachment(attachment: Any): Builder {
            this.attachment = attachment
            return self()
        }

        override fun build(): MessageViewModel {
            return MessageViewModel(this)
        }
    }
}
