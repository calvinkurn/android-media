package com.tokopedia.product.detail.data.model.addtocartrecommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.detail.view.adapter.AddToCartDoneTypeFactory

data class AddToCartDoneFreeOngkirTitle(
        var freeOngkirTitle: String = ""
) : Visitable<AddToCartDoneTypeFactory> {

    override fun type(typeFactory: AddToCartDoneTypeFactory): Int {
        return typeFactory.type(this)
    }
}