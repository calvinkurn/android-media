package com.tokopedia.talk_old.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk_old.producttalk.view.adapter.ProductTalkListTypeFactory

/**
 * @author by Steven
 */

class EmptyProductTalkViewModel(val isMyShop : Boolean) : Visitable<ProductTalkListTypeFactory> {

    constructor() : this(false)

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }

}