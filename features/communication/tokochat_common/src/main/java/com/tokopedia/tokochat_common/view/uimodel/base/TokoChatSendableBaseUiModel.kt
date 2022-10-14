package com.tokopedia.tokochat_common.view.uimodel.base

import com.tokopedia.tokochat_common.util.TokoChatValueUtil

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatSendableBaseUiModel constructor(
    builder: Builder<*, *>
) : TokoChatBaseUiModel(builder) {

    var messageStatus: Int = builder.messageStatus
    var isDummy: Boolean = builder.isDummy
    val isSender: Boolean = builder.isSender

    fun isRead(): Boolean {
        return messageStatus == TokoChatValueUtil.READ_VALUE
    }

    abstract class Builder<
        out B : Builder<B, UI>,
        out UI : TokoChatSendableBaseUiModel
        > : TokoChatBaseUiModel.Builder<B, UI>() {

        internal var messageStatus: Int = TokoChatValueUtil.PENDING_VALUE
        internal var isDummy: Boolean = false
        internal var isSender: Boolean = true

        fun withMessageStatus(messageStatus: Int): B {
            this.messageStatus = messageStatus
            return self()
        }

        fun withIsDummy(isDummy: Boolean): B {
            this.isDummy = isDummy
            return self()
        }

        fun withIsSender(isSender: Boolean): B {
            this.isSender = isSender
            return self()
        }
    }
}
