package com.tokopedia.shop.product.view.model

import com.tokopedia.shop.common.data.viewmodel.BaseMembershipViewModel
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

class FreeOngkirTickerViewModel(
        var title: String = "QWE",
        var description: String = "ASD"
) : BaseShopProductViewModel {
    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int = typeFactory.type(this)
}