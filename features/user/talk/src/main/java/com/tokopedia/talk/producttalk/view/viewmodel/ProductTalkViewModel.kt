package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkThreadTypeFactory


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
