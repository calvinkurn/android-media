package com.tokopedia.addongifting.addonbottomsheet.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.addongifting.addonbottomsheet.view.adapter.AddOnListAdapterTypeFactory

data class ProductUiModel(
        var isTokoCabang: Boolean = false,
        var shopName: String = "",
        var mainProductImageUrl: String = "",
        var mainProductName: String = "",
        var mainProductQuantity: Int = 0,
        var mainProductPrice: Long = 0,
        var otherProductCount: Int = 0,
        var promoMessage: String = ""
) : Visitable<AddOnListAdapterTypeFactory> {

    override fun type(typeFactory: AddOnListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}