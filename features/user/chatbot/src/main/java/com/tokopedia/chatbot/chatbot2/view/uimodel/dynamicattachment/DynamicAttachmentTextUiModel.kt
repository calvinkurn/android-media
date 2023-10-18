package com.tokopedia.chatbot.chatbot2.view.uimodel.dynamicattachment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.adapter.ChatbotTypeFactory
import com.tokopedia.kotlin.extensions.view.toBlankOrString

class DynamicAttachmentTextUiModel(
    builder: Builder
) : SendableUiModel(builder), Visitable<ChatbotTypeFactory> {

    var rejectReasons: DynamicAttachmentRejectReasons? = builder.rejectReasons

    class Builder : SendableUiModel.Builder<Builder, DynamicAttachmentTextUiModel>() {

        internal var message: String? = null
        internal var rejectReasons: DynamicAttachmentRejectReasons? = null
        fun withMsgContent(message: String?): Builder {
            this.message = message
            return self()
        }

        fun isSender(isSender: Boolean): Builder {
            this.isSender = isSender
            return self()
        }

        fun withRejectReasons(rejectReasons: DynamicAttachmentRejectReasons): Builder {
            this.rejectReasons = rejectReasons
            return self()
        }

        override fun build(): DynamicAttachmentTextUiModel {
            return DynamicAttachmentTextUiModel(
                this
            )
        }
    }

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}

fun DynamicAttachmentTextUiModel.toMessageUiModel(): MessageUiModel {
    return MessageUiModel.Builder()
        .withMsg(this.message)
        .withMsgId(this.messageId)
        .withFromUid(this.fromUid.toBlankOrString())
        .withFrom(this.from)
        .withFromRole(this.fromRole)
        .withAttachmentId(this.attachmentId)
        .withAttachmentType(this.attachmentType)
        .withReplyTime(this.replyTime.toBlankOrString()).withMsg(this.message)
        .withSource(this.source).withLabel(this.label).withOrGenerateLocalId(this.localId)
        .withParentReply(this.parentReply).withFraudStatus(this.fraudStatus)
        .withTickerReminder(this.tickerReminder)
        .build()
}
