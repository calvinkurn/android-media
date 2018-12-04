package com.tokopedia.talk.producttalk.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.talk.producttalk.view.adapter.ProductTalkListTypeFactory

class LoadProductTalkThreadViewModel(

) : Visitable<ProductTalkListTypeFactory> {

    override fun type(typeFactory: ProductTalkListTypeFactory): Int {
        return typeFactory.type(this)
    }


}
