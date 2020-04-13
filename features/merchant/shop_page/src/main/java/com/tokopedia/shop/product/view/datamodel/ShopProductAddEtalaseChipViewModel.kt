package com.tokopedia.shop.product.view.datamodel

import com.tokopedia.shop.product.view.adapter.ShopProductEtalaseAdapterTypeFactory

/**
 * Created by normansyahputa on 2/28/18.
 */

class ShopProductAddEtalaseChipViewModel : BaseShopProductEtalaseViewModel {
    override fun type(typeFactory: ShopProductEtalaseAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
