package com.tokopedia.chatbot.data.dynamicattachment.dynamicstickybutton

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

class DynamicAttachmentTextUiModel(
    builder: Builder
) : SendableUiModel(builder), Visitable<ChatbotTypeFactory> {

    class Builder : SendableUiModel.Builder<Builder, DynamicAttachmentTextUiModel>() {

        internal var message: String? = null
        fun withMsgContent(message: String?): Builder {
            this.message = message
            return self()
        }

        override fun build(): DynamicAttachmentTextUiModel {
            return DynamicAttachmentTextUiModel(this)
        }

    }

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }
}
