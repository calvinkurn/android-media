package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

class HideUiModel : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}