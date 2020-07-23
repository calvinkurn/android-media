package com.tokopedia.talk_old.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.producttalk.view.adapter.ProductTalkListTypeFactory


/**
 * @author by Steven.
 */

data class TalkThreadViewModel(
        var headThread: ProductTalkItemViewModel,
        var listChild: ArrayList<Visitable<*>>
) : ProductTalkListViewModel() {

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
