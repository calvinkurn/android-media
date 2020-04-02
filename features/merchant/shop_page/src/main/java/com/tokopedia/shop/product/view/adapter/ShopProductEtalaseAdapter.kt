package com.tokopedia.shop.product.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.product.view.datamodel.BaseShopProductEtalaseViewModel

class ShopProductEtalaseAdapter(
        shopEtalaseAdapterTypeFactory: ShopProductEtalaseAdapterTypeFactory
) : BaseListAdapter<BaseShopProductEtalaseViewModel, ShopProductEtalaseAdapterTypeFactory>(shopEtalaseAdapterTypeFactory) {
    var selectedEtalaseId: String = ""

    init {
        shopEtalaseAdapterTypeFactory.attachAdapter(this)
    }

    fun isEmptyVisit():Boolean = visitables.isEmpty()

}
