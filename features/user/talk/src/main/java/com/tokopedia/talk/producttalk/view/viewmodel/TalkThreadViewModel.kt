package com.tokopedia.talk

import com.tokopedia.abstraction.base.view.adapter.Visitable


/**
 * @author by Steven.
 */

data class TalkThreadViewModel(
        var headThread: ProductTalkItemViewModel,
        var listChild: ArrayList<Visitable<*>>
) : ProductTalkListViewModel() {

    override fun type(typeFactory: ProductTalkThreadTypeFactory): Int {
        return typeFactory.type(this)
    }
}
