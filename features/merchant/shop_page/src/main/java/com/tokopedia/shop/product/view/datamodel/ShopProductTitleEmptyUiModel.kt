package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

class ShopProductTitleEmptyUiModel: BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}