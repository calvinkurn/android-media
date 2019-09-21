package com.tokopedia.shop.search.view.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.search.view.adapter.model.ShopSearchProductDataModel

class ShopSearchProductAdapter(
        factory: ShopProductAdapterTypeFactory
) : BaseListAdapter<ShopSearchProductDataModel, ShopProductAdapterTypeFactory>(factory)