package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkChildThreadTypeFactory
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkThreadTypeFactory


/**
 * @author by Steven.
 */

data class ProductTalkThreadViewModel(
        var headThread: ProductTalkItemViewModel,
        var listChild: List<Visitable<ProductTalkChildThreadTypeFactory>>
) : ProductTalkListViewModel(){

    override fun type(typeFactory: ProductTalkThreadTypeFactory): Int {
        return typeFactory.type(this)
    }
}
