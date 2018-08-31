package com.tokopedia.talk.inboxtalk.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.ProductTalkThreadViewModel
import com.tokopedia.talk.inboxtalk.adapter.InboxTalkTypeFactory


/**
 * @author by nisie on 8/30/18.
 */
data class InboxTalkItemViewModel(
        var productHeader: ProductHeader,
        var talkThread : ProductTalkThreadViewModel
) : Visitable<InboxTalkTypeFactory> {

    override fun type(typeFactory: InboxTalkTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class ProductHeader(
        val productName: String,
        val productAvatar : String
)
