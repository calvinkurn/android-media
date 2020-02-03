package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by zulfikarrahman on 1/16/18.
 */

data class ShopProductEtalaseTitleViewModel(
        var etalaseName: String,
        var etalaseBadge: String
) : BaseShopProductViewModel {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
