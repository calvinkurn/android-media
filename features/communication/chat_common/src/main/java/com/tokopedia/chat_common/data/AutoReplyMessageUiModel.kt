package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

class AutoReplyMessageUiModel private constructor(
    builder: MessageUiModel.Builder
): MessageUiModel(builder), Visitable<BaseChatTypeFactory> {

    val impressHolder = ImpressHolder()
    
    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder: MessageUiModel.Builder() {

        override fun build(): MessageUiModel {
            return AutoReplyMessageUiModel(this)
        }
    }
}
