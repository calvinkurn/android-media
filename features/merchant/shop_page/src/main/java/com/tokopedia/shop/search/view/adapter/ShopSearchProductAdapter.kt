package com.tokopedia.shop.search.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.search.view.model.ShopSearchProductDataModel

class ShopSearchProductAdapter(shopProductAdapterTypeFactory: ShopProductAdapterTypeFactory)
    : BaseListAdapter<ShopSearchProductDataModel, ShopProductAdapterTypeFactory>(
        shopProductAdapterTypeFactory
) {
    
}