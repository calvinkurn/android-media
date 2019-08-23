package com.tokopedia.shop.open.data.model

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class PostalCodeViewModel(var postalCode:String = "")
    : Visitable<PostalCodeTypeFactory> {

    override fun type(typeFactory: PostalCodeTypeFactory): Int {
        return typeFactory.type(this)
    }
}