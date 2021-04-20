package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

data class ShopEmptyProductUiModel(
        val isMyShop: Boolean,
        val title : String,
        val description: String
) : Visitable<ShopProductAdapterTypeFactory> {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}