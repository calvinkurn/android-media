package com.tokopedia.shop.product.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

data class EmptyOwnShopModel(
        var title: String = "",
        var content: String = ""
) : Visitable<ShopProductAdapterTypeFactory> {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}