package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by yfsx on 08/05/18.
 */

class FallbackAttachmentUiModel private constructor(
    builder: MessageUiModel.Builder
) : MessageUiModel(builder),
    Visitable<BaseChatTypeFactory> {

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : MessageUiModel.Builder() {
        override fun build(): MessageUiModel {
            return FallbackAttachmentUiModel(this)
        }
    }
}
