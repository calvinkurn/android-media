package com.tokopedia.talk

import com.tokopedia.abstraction.base.view.adapter.Visitable


/**
 * @author by Steven.
 */

data class ProductTalkViewModel(
        var listThread: List<Visitable<ProductTalkThreadTypeFactory>>
) : Visitable<ProductTalkThreadTypeFactory> {

    override fun type(typeFactory: ProductTalkThreadTypeFactory): Int {
        return typeFactory.type(this)
    }
}
