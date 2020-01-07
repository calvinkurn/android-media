package com.tokopedia.shop.newproduct.view.datamodel

import com.tokopedia.shop.newproduct.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by nathan on 2/6/18.
 */

class ShopProductAddViewModel : BaseShopProductViewModel {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
