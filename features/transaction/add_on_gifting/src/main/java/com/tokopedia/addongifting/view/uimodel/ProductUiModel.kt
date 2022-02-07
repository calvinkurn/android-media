package com.tokopedia.addongifting.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.addongifting.view.adapter.AddOnListAdapterTypeFactory

data class ProductUiModel(
        var isTokoCabang: Boolean = false,
        var shopTier: Int = 0,
        var shopName: String = "",
        var mainProductImageUrl: String = "",
        var mainProductName: String = "",
        var mainProductPrice: Long = 0,
        var otherProductCount: Int = 0
) : Visitable<AddOnListAdapterTypeFactory> {

    override fun type(typeFactory: AddOnListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}