package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.Reply
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * @author by yfsx on 08/05/18.
 */

class FallbackAttachmentViewModel private constructor(
    builder: MessageViewModel.Builder
) : MessageViewModel(builder),
    Visitable<BaseChatTypeFactory> {

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    class Builder : MessageViewModel.Builder() {
        override fun build(): MessageViewModel {
            return FallbackAttachmentViewModel(this)
        }
    }
}
