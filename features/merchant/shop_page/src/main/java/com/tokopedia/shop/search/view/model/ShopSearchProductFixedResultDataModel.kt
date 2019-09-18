package com.tokopedia.shop.search.view.model

import com.tokopedia.shop.R
import com.tokopedia.shop.product.view.adapter.ShopProductAdapterTypeFactory
import com.tokopedia.shop.search.view.adapter.ShopSearchProductAdapterTypeFactory

class ShopSearchProductFixedResultDataModel : ShopSearchProductDataModel() {

    companion object {
        val LAYOUT = R.layout.shop_search_product_fixed_result_layout
    }

    override fun type(
            typeFactory: ShopSearchProductAdapterTypeFactory
    ) = typeFactory.type(this)



}