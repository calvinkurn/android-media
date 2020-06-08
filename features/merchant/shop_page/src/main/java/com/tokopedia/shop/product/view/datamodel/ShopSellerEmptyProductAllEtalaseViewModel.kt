package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory

/**
 * Created by nathan on 2/6/18.
 */

class ShopSellerEmptyProductAllEtalaseViewModel : BaseShopProductViewModel {

    override fun type(typeFactory: ShopProductAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
