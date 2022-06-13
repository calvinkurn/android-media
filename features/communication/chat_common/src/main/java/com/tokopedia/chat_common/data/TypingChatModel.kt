package com.tokopedia.chat_common.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.view.adapter.BaseChatTypeFactory

/**
 * Created by stevenfredian on 10/26/17.
 */

class TypingChatModel : Visitable<BaseChatTypeFactory> {

    var logo: String? = null

    override fun type(typeFactory: BaseChatTypeFactory): Int {
        return typeFactory.type(this)
    }

}
