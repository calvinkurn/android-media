package com.tokopedia.tokochat.common.view.chatroom.uimodel.base

import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil

/**
 * Primary constructor, use [Builder] class to create this instance.
 * Extend, do not edit
 */
open class TokoChatSendableBaseUiModel constructor(
    builder: Builder<*, *>
) : TokoChatBaseUiModel(builder) {

    var messageStatus: Int = builder.messageStatus
    val isSender: Boolean = builder.isSender

    fun isRead(): Boolean {
        return messageStatus == TokoChatCommonValueUtil.READ_VALUE
    }

    fun isDummy(): Boolean {
        return messageStatus == TokoChatCommonValueUtil.PENDING_VALUE
    }

    fun isSent(): Boolean {
        return messageStatus == TokoChatCommonValueUtil.SENT_VALUE
    }

    fun isFailed(): Boolean {
        return messageStatus == TokoChatCommonValueUtil.FAILED_VALUE
    }

    abstract class Builder<
        out B : Builder<B, UI>,
        out UI : TokoChatSendableBaseUiModel
        > : TokoChatBaseUiModel.Builder<B, UI>() {

        internal var messageStatus: Int = TokoChatCommonValueUtil.PENDING_VALUE
        internal var isSender: Boolean = true

        fun withMessageStatus(messageStatus: Int): B {
            this.messageStatus = messageStatus
            return self()
        }

        fun withIsSender(isSender: Boolean): B {
            this.isSender = isSender
            return self()
        }
    }
}
