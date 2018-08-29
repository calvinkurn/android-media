package com.tokopedia.talk

import com.tokopedia.abstraction.base.view.adapter.Visitable

class LoadProductTalkViewModel(
        var counter: Int,
        var text: String
) : Visitable<ProductTalkChildThreadTypeFactory> {

    override fun type(typeFactory: ProductTalkChildThreadTypeFactory): Int {
        return typeFactory.type(this)
    }


}
