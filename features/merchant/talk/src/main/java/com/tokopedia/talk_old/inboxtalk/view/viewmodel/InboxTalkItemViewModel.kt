package com.tokopedia.talk_old.inboxtalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.inboxtalk.view.adapter.InboxTalkTypeFactory
import com.tokopedia.talk_old.producttalk.view.viewmodel.TalkThreadViewModel


/**
 * @author by nisie on 8/30/18.
 */
data class InboxTalkItemViewModel(
        var productHeader: ProductHeader,
        var talkThread: TalkThreadViewModel
) : Visitable<InboxTalkTypeFactory> {

    override fun type(typeFactory: InboxTalkTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class ProductHeader(
        val productName: String = "",
        val productAvatar: String = "",
        val productId: String = "",
        val productApplink: String = ""
)
