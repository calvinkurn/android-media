package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory

class EmptyOwnShopModel : Visitable<ShopProductAdapterTypeFactory> {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}